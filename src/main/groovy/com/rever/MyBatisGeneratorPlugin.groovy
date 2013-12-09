package com.rever;

import groovy.transform.ToString
import org.gradle.api.Plugin
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.tasks.TaskAction

class MyBatisGeneratorPlugin implements Plugin<ProjectInternal> {
    @Override
    void apply(ProjectInternal project) {
        project.logger.info "Configuring MyBatis Generator for project: $project.name"
        MyBatisGeneratorTask task = project.tasks.create("mbGenerator", MyBatisGeneratorTask);
        project.configurations.create('mybatis').with {
            description = 'The cargo libraries to be used for this project.'
        }
        project.extensions.create("mybatis", MbgExtension)

        task.conventionMapping.with {
            myBatisGeneratorClasspath = {
                def config = project.configurations['mybatis']
                if (config.dependencies.empty) {
                    project.dependencies {
                        mybatis('org.mybatis.generator:mybatis-generator-core:1.3.2')
                    }
                }
                config
            }
            overwrite = { project.mybatis.overwrite }
            configFile = { project.mybatis.configFile }
            verbose = { project.mybatis.verbose }
            targetDir = { project.mybatis.targetDir }
        }

    }
}

@ToString(includeNames = true)
class MbgExtension {
    def overwrite = true
    def configFile = "generatorConfig.xml"
    def verbose = false
    def targetDir = "."
}

class MyBatisGeneratorTask extends ConventionTask {
    def overwrite
    def configFile
    def verbose
    def targetDir
    FileCollection myBatisGeneratorClasspath

    @TaskAction
    void executeCargoAction() {
        services.get(IsolatedAntBuilder).withClasspath(getMyBatisGeneratorClasspath()).execute {
            ant.taskdef(name: 'mbgenerator', classname: 'org.mybatis.generator.ant.GeneratorAntTask')

            ant.properties['generated.source.dir'] = getTargetDir()
            ant.mbgenerator(overwrite: getOverwrite(), configfile: getConfigFile(), verbose: getVerbose()) {
//                propertyset(propertyref("generated.source.dir"))
            }
        }
    }
}
