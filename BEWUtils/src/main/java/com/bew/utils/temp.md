
To setup in a new project:

1. Create a new file: "pom.properties"  
    Location: "src/main/resources"
2. Place the following text into that file, and save it:
```
title=${project.name}
description=${project.description}
artifactId=${project.artifactId}
groupId=${project.groupId}
version=${project.version}
```
3. Add the following to your projects __pom.xml__ file:
```
<build>

    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
            <includes>
                <include>**/pom.properties</include>
            </includes>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>false</filtering>
            <excludes>
                <exclude>**/pom.properties</exclude>
            </excludes>
        </resource>
    </resources>

</build>
```
