MyBatisGenerator
================

Gradle plugin of MyBatisGenerator

You can apply the plugin using the following buildscript directly from github:

```
buildscript {
  apply from: 'https://raw.github.com/reverocean/MyBatisGenerator/master/repo/com/rever/mybatisPlugin/1.0-SNAPSHOT/mybatis.gradle'
}
```

You can config the [generator config file](http://mybatis.org/generator/configreference/xmlconfig.html) by the following:
```
mybatis{
    configFile = '../generatorConfig.xml'
}
```


