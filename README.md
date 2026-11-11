# Employee Task Management System

A Java Swing desktop application for assigning work to employees and tracking their productivity. Tasks can be simple (a time slot within a day) or complex (a group of other tasks), and all data is persisted between runs using Java serialization.

## Features

- Add employees and tasks, with duplicate-ID checks for both
- **Simple tasks** defined by a start and end hour (0-23, overnight spans are handled)
- **Complex tasks** built from existing simple tasks, whose duration is the sum of their sub-tasks
- Assign tasks to employees and toggle a task between *Completed* and *Uncompleted*
- Calculate an employee's total work duration (completed tasks only)
- View all employees with their tasks, and view unassigned tasks
- Statistics of completed vs. uncompleted tasks per employee
- Filter employees who worked more than 40 hours, sorted ascending by duration
- Automatic save/load of the application state through `ObjectOutputStream` / `ObjectInputStream`

## Design

| Concept | Where |
|---|---|
| **Composite pattern** - `Task` is the component, `SimpleTask` the leaf, `ComplexTask` the composite | `DataModel` |
| **MVC** - `View` (Swing UI), `Controller` (event handling), business model | `GUI`, `Business` |
| **Serialization** - state saved to `tasks_data.txt` (git-ignored) | `Data` |
| Business rules, statistics and filtering | `Business` (`TasksManagement`, `Utility`) |

```
src/main/java/org/tucn/pt
├── Business/     TasksManagement, Utility
├── DataModel/    Task, SimpleTask, ComplexTask, Employee
├── Data/         SerializationOperations
└── GUI/          Main, View, Controller
```

## Getting started

Requirements: JDK 25 (as configured in `pom.xml`; lower `maven.compiler.source/target` if you use an older JDK) and Maven.

```bash
mvn compile exec:java -Dexec.mainClass=org.tucn.pt.GUI.Main
```

Or open the project in IntelliJ IDEA and run `org.tucn.pt.GUI.Main`.
