# MARS-OPS

## Autonomous Mars Colony Management and Emergency Simulator

MARS-OPS is a Java simulation of a small Mars colony. It brings together crew members, robots, facilities, resources, missions, environmental conditions, and emergencies. The program demonstrates how these objects collaborate and how their state changes as simulated time advances.

The application is started by `Main.java`. `MarsColony` connects the system's objects and routes requests to the classes responsible for handling them. Mission rules live in the mission package, agent behavior lives in the agent classes, facility stock lives in facilities, and emergency behavior lives in emergency subclasses.

## Project structure

```text
Main.java
person1/                 Colony, environment, simulation and reporting
person2/                 Agent interface, crew, robots and capabilities
person3/facilities/      Locations, facilities, equipment and resources
person3/emergencies/     Emergency lifecycle and emergency types
person3/umls/            Existing editable draw.io diagrams
person4/                 Mission types, states and mission manager
```

### Person 1: core system

- `Main` builds the example colony, runs the demonstration scenarios, prints results and checks expected outcomes.
- `MarsColony` stores the colony's crew, robots, facilities, environment, resource pool, mission manager, emergency log and simulation time. It routes mission and emergency requests.
- `Simulation` advances the logical clock and sends time updates.
- `Environment` and `EnvironmentCondition` represent NORMAL, DUST_STORM and COMM_DISRUPT conditions.
- `ColonyReport` creates a readable snapshot of the colony.
- `OperationResult` carries a success value and a reason for operations that can fail.
- `Tickable` defines the `onTick(MarsColony)` update method.

### Person 2: agents

- `Agent` is the shared interface for people and robots.
- `CrewMember` and `Robot` are abstract base classes for shared human and robot data and behavior.
- `Scientist`, `Engineer` and `MedicalOfficer` specialize crew behavior.
- `ExplorationRover`, `RepairRobot` and `Drone` specialize robot behavior.
- `Capability` lists work abilities such as RESEARCH, REPAIR and SURFACE_TRAVEL.

Missions and emergencies ask agents whether they can perform work or handle an event. They use the `Agent` interface instead of checking concrete class names.

### Person 3: facilities, resources and emergencies

- `Location` is a shared interface for places. `SurfaceLocation` represents the Martian surface.
- `Facility` is the abstract parent of `Habitat`, `Laboratory`, `StorageFacility`, `PowerStation` and `CommunicationStation`.
- `Equipment` tracks whether an item is working. `LifeSupportSystem` is owned by a habitat.
- `ResourceType` and `FacilityStatus` are enums for resource kinds and facility states.
- `ResourcePool` calculates colony-wide stock and consumes resources from facilities.
- `Emergency` contains the common response flow. `OxygenLeak`, `Fire`, `RoverBreakdown` and `Medical` implement event-specific effects.
- `EmergencyType` and `EmergencyState` represent emergency categories and outcomes.

### Person 4: missions

- `Mission` is the abstract base for mission requirements, assigned agents and lifecycle state.
- `ExplorationMission`, `ResearchMission`, `RepairMission` and `RescueMission` provide mission-specific requirements.
- `MissionManager` validates mission submissions, enforces the one-active-mission rule, advances the active mission, releases agents and records history.
- `MissionState` lists ASSIGNED, IN_PROGRESS, COMPLETED and FAILED.

## How the system works

### Building the colony

`Main.buildColony()` creates a fresh set of objects for each scenario. It creates facilities first, then a `ResourcePool` and `MissionManager`, then the `MarsColony`, agents and `Simulation`. The resource pool receives a copy of the facility list, so the setup adds all facilities before constructing the pool.

### Submitting and running a mission

1. `Main` creates a mission and a proposed team.
2. `MarsColony.submitMission()` checks basic input and forwards the request to `MissionManager`.
3. `MissionManager.submit()` checks whether a mission is already active, whether the environment allows the operation, whether target facilities are available, whether the team and each agent qualify, whether required equipment is present, and whether resources can be supplied.
4. If any check fails, the mission becomes FAILED with a reason and is added to history. It does not become active; agents are not reserved and mission resources are not consumed.
5. If every check passes, resources are consumed, agents become unavailable, the mission becomes ASSIGNED and the manager stores it as active.
6. Simulation ticks start and advance the mission. Once complete or failed, the manager releases the agents, clears the active mission and records the outcome.

Mission resources are consumed when the mission is accepted. The current code does not refund them if an accepted mission fails later.

### Time units

A time unit is one logical simulation step, not a real-world second or minute. `Simulation.advanceTime(n)` repeats the same update `n` times. Each tick:

1. Increments the time and updates the colony's time value.
2. Calls `MissionManager.onTick()`.
3. Calls `onTick()` on every crew member.
4. Calls `onTick()` on every robot.

The manager checks for mission failure first. On the first safe tick, an ASSIGNED mission becomes IN_PROGRESS and returns without increasing elapsed time. Later ticks increase elapsed time. Therefore a mission with duration 3 takes four ticks after acceptance: one start tick and three progress ticks.

Crew fatigue and robot battery are also updated each tick. Available crew recover fatigue; busy crew gain fatigue. Robots drain less battery while available and more while busy. The environment and facilities are not ticked.

### Resources

Facilities store their own resource quantities. `ResourcePool` provides the colony-wide view:

- `getTotal(type)` sums quantities held by operational facilities only.
- `canSupply(...)` checks whether the full request is available.
- `useSupply(...)` checks first and removes stock only if the full request can be supplied. It can draw from multiple facilities.

This check-before-consume order prevents partial spending when the colony lacks enough resources. The `Main.cost()` helper creates an `int[]` sized to `ResourceType.values().length` and places a cost at the matching enum ordinal. Keep that array length consistent with the resource enum order.

### Emergencies

`MarsColony.triggerEmergency()` records the emergency and calls `Emergency.handle(colony)`. The common handler applies the detection effect, finds available agents with the required capabilities, checks response resources, marks responders unavailable during the response, runs the success or unresolved behavior, and releases responders in a `finally` block.

| Emergency | Detection and response in this implementation |
| --- | --- |
| Oxygen leak | Deducts the specified oxygen from the affected habitat at detection. Requires REPAIR capability and one SPARE_PART. If unresolved, deducts the same oxygen amount a second time. |
| Fire | Marks the laboratory on fire and unavailable and damages its equipment. Requires FIRE_RESPONSE and one OXYGEN. A successful response extinguishes the fire. |
| Rover breakdown | Damages the robot and fails any active mission. Requires REPAIR and one SPARE_PART. A successful response repairs the robot. |
| Medical | Requires MEDICAL and one MEDICAL_SUPPLIES. A successful response increases the patient's health by 20. |

Emergencies do not use the one-active-mission slot. However, agents already assigned to a mission are unavailable and cannot respond.

### Environmental conditions

Only one `EnvironmentCondition` is active at a time. It starts as NORMAL and changes when `setCondition()` is called. A null condition is ignored.

- DUST_STORM blocks surface operations. A surface mission is rejected at submission or may fail during a later tick if a storm begins while it is active.
- COMM_DISRUPT blocks remote control. A `Drone` refuses mission work while communications are disrupted. Rescue missions also require communications, which `MissionManager` checks.
- NORMAL permits surface operations and remote control.

## Object-oriented design

- **Encapsulation:** most state is stored in private fields and changed through methods such as `useResource()`, `assign()`, `fail()` and `setAvailable()`.
- **Abstraction:** interfaces such as `Agent`, `Location` and `Tickable` describe shared behavior without specifying each implementation.
- **Inheritance:** specialized classes reuse common behavior, for example `Engineer extends CrewMember` and `ExplorationMission extends Mission`.
- **Polymorphism:** callers use shared types and Java invokes the concrete implementation, for example calling `canPerform()` through an `Agent` reference.
- **Aggregation:** `MarsColony` groups crew, robots and facilities that are created outside the colony and can exist independently.
- **Composition:** `Habitat` creates and owns its `LifeSupportSystem`.
- **Association:** missions refer to assigned agents; managers refer to the active mission and shared services.

## Mission states

- **Not submitted:** represented by `null` in this implementation.
- **ASSIGNED:** accepted by the manager and waiting for its first tick.
- **IN_PROGRESS:** started and advancing on later ticks.
- **COMPLETED:** elapsed time reaches the mission duration.
- **FAILED:** submission is rejected or an active mission fails a later check.

Rejected missions go directly to FAILED and never become active. Active missions may fail because of environmental restrictions, an inoperable assigned agent, or unavailable target facilities. Failed missions are not retried; create a new mission object for another attempt.

## Running the program

Requirements: JDK 17 or newer.

From the project root in Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$sourceFiles = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $sourceFiles
java -cp out Main
```

The main class is in the default package at the project root. Other classes use the `person1`, `person2`, `person3.facilities`, `person3.emergencies` and `person4` packages.

## Demonstration scenarios

`Main` runs eleven scenarios. Each one builds a fresh colony so state from one scenario does not affect another.

| # | Scenario | What it demonstrates |
| --- | --- | --- |
| 1 | Successful exploration | A scientist and rover are accepted; water is consumed; the mission completes and agents are released. |
| 2 | Unsuitable agent | An unsuitable team is rejected with a capability reason; the mission never becomes active. |
| 3 | Resource shortage | A request for 5,000 water is rejected; no water is consumed and stock does not become negative. |
| 4 | Oxygen leak | The habitat loses the specified oxygen; repair responders use one spare part and resolve the leak. |
| 5 | Dust storm | Surface exploration is rejected with an environmental reason. |
| 6 | Second mission while one is active | The first mission remains active; the second is rejected. |
| 7 | Unresolved oxygen leak | With engineer and repair robot unavailable, the leak is unresolved, oxygen loss is doubled, and no spare part is consumed. |
| 8 | Several time units | Shows that the first tick starts a mission and later ticks advance it. |
| 9 | Zero time units | Advancing zero units leaves simulation time unchanged. |
| 10 | Storm during a mission | A surface mission fails after the environment changes to a dust storm; agents are released. |
| 11 | Idle robot battery | An idle robot loses one battery point per tick. |

Each check prints `[PASS]` or `[FAIL]`. The program catches scenario exceptions and prints a final check summary.

## Implementation notes and limitations

- `ResourcePool` copies the facility list during construction. Adding a new facility to `MarsColony` later does not add it to the pool's list. The supplied `Main` avoids this by creating the full facility list before creating the pool.
- `Mission` uses `null` as its pre-submission state. The named `MissionState` values begin after submission.
- The mission team's `covers()` check looks for required capabilities across the whole team. Each agent's `canPerform()` checks that the agent contributes at least one required capability, along with that agent's own health/skill or battery rules.
- `RepairMission.targetsUnavailable()` currently always returns false, so it bypasses the base target-facility availability check.
- The current OxygenLeak handler selects available agents with repair capability but does not explicitly require one human and one robot. Responders are not moved to the emergency location.
- An emergency's stored `reason` is its initial description. A failure reason is returned in `OperationResult`, but it is not saved separately in the Emergency object.
- Fire's unresolved hook currently has no extra effect; the laboratory remains unavailable and on fire.
- `Medical` stores a facility reference, but the current implementation does not use it in the response checks.
- `PowerStation` and `CommunicationStation` currently add no behavior beyond `Facility`.

These notes describe the current Java implementation and may differ from broader requirements in the lab specification.
