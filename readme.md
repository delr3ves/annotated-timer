# Annotated Timer

## Getting started

Just add the dependency in your pom.xml 

    <dependencies>
        ...
        <dependency>
            <groupId>io.delr3ves</groupId>
            <artifactId>annotated-timer</artifactId>
            <version>1.0.0</version>
        </dependency>
        ...
    </dependencies>
    
Then create the injector using *io.delr3ves.metrics.AnnotatedTimerGuiceModule* or install it in your custom module:

    Injector injector = Guice.createInjector(new AnnotatedTimerGuiceModule(metricRegistry, excludedClasses, excludedPackages));

or 
    public class YourCustomModule extends AbstractModule {

        @Override
        protected void configure() {
            ...
            install(new AnnotatedTimerGuiceModule(metricRegistry, excludedClasses, excludedPackages))
            ...
        }

    }

After that, you can measure the execution time of every annotated method  of objects provided by the injector.  

