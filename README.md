# Memory Example Project

## Code Coverage

The project currently maintains about **90% line coverage**, enforced using the **JaCoCo Maven Plugin**.
- Some packages are excluded from coverage checks:
  - `util`
  - `helper`
  - `Main` classes

### Generate Coverage Report
```mvn clean test```  

### Run Application
```mvn clean compile exec:java``` 
