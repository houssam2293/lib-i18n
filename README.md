
Lib-I18n
===

[![Build Status](https://travis-ci.org/Naoghuman/lib-i18n.svg?branch=master)](https://travis-ci.org/Naoghuman/lib-i18n)
[![license: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![GitHub release](https://img.shields.io/github/release/Naoghuman/lib-i18n.svg)](https://GitHub.com/Naoghuman/lib-i18n/releases/)



Intention
---

The library `Lib-I18N` allows a developer to bind a key-value pair of a `.properties` 
file to a [StringBinding]. This makes it very easy to change the language during 
runtime in a [JavaFX] application.  
Lib-I18N is written in JavaFX, [Maven] and [NetBeans].

_Image:_ Demo integration from Lib-I18N  
![Lib-I18N_Demo_v0.8.0_2019-04-27_16-24.png][Lib-I18N_Demo_v0.8.0_2019-04-27_16-24]

The demo shows how easy an application becomes multilingual in four steps :smile: .  
Plz see the section `Demo` for more informations.



Content
---

* [Features](#Features)
    - [Main library features](#MaLiFe)
    - [General library features](#GeLiFe)
* [Conventions](#Conventions)
    - [Convention: 'baseBundleName' from ResourceBundle](#CoBaFrRe)
    - [Convention: 'Key not found' in ResourceBundle](#CoKeInReBu)
    - [Convention: Defined supported Locales, default and actual Locale](#CoDeSuDeAcLo)
    - [Convention: Basic validation](#CoBaVa)
* [Examples](#Examples)
    - [How to use the builder I18NResourceBundleBuilder](#HoToUsReBuBu)
    - [How to use the builder I18NBindingBuilder](#HoToUsBiBu)
    - [How to use the builder I18NMessageBuilder](#HoToUsMeBu)
* [Demo](#Demo)
    - [Step one: Prepare your application](#DePrYoAp)
    - [Step two: Register the ResourceBundle](#DeStTw)
    - [Step three: Bind the text components](#DeStTh)
    - [Step four: Switch the language during runtime](#DeStFo)
* [JavaDoc](#JavaDoc)
* [Download](#Download)
* [Requirements](#Requirements)
* [Installation](#Installation)
* [Contribution](#Contribution)
* [License](#License)
* [Autor](#Autor)
* [Contact](#Contact)



Features<a name="Features" />
---

### Main library features<a name="MaLiFe" />

In this sub-section all main features from the library 'Lib-I18N' are listed:

1. The library `Lib-I18N` allows a developer to bind a key with its associated value
   of a `.properties` file to a [StringBinding].
2. With the builder [I18NResourceBundleBuilder] the developer can configure the 
   [ResourceBundle] which contains the `key - value` pairs which will then be bind 
   to a `actual` [Locale].
3. The builder [I18NBindingBuilder] let the developer create a [StringBinding]. The 
   StringBinding can created with a function from type [Callable&lt;String&gt;] or 
   with a .properties `key` and optional `arguments`.
4. To load a .properties `key` with optional `arguments` from the initialized 
   [ResourceBundle] through the [I18NResourceBundleBuilder] the developer can use 
   the builder [I18NMessageBuilder].


### General library features<a name="GeLiFe" />

This sub-section list all general features from the library 'Lib-I18N':

1. The library `Lib-I18N` is `open source` and licensed under [General Public License 3.0].
2. The library is written in `Java` and [JavaFX].
3. The library is programmed with the IDE [NetBeans] as a [Maven] library.
4. The library can easily integrated into a foreign project over [Maven Central].
5. Due to the connection of the project with [Travis CI], a build is automatically 
   executed at each commit.
6. By integrating various "badges" from "img.shield.io", interested readers can 
   easily find out about the "build" state, the current version and the license 
   used for this library.
7. All functionalities from the classes in the `core` and `internal` packages are 
   tested with `Unittests`.
8. Every `parameter` in all functionalities will be verified against minimal 
   conditions with the internal validator [DefaultI18NValidator]. For example a 
   `String` can't be `NULL` or `EMPTY`.
9. The documentation from the library is available with an extended `ReadMe` and 
   well-described [JavaDoc] comments.



Conventions<a name="Conventions" />
---

In this chapter, the interested developer can find out all about the conventions 
from the library `Lib-I18N`:

### Convention: 'baseBundleName' from ResourceBundle<a name="CoBaFrRe" />

If a [ResourceBundle] with the defined 'baseBundleName' can't be found a spezific 
  MissingResourceException will be thrown.

```java
public final class DefaultI18NResourceBundle implements I18NResourceBundle {
    ...
    private ResourceBundle getResourceBundle() {
        ResourceBundle bundle = null;
        try {
            bundle = ResourceBundle.getBundle(this.getBaseBundleName(), this.getActualLocale());
        } catch (MissingResourceException mre) {
            LoggerFacade.getDefault().error(this.getClass(), 
                    String.format("Can't access the ResourceBundle[path=%s, actual-locale=%s]", 
                            this.getBaseBundleName(), this.getActualLocale().getDisplayLanguage()), 
                    mre);
        }
        
        return bundle;
    }
    ...
}
```

### Convention: 'Key not found' in ResourceBundle<a name="CoKeInReBu" />

If a `key` can't be found in the defined ResourceBundle then  
* the String pattern '&lt;key&gt;' will returned and
* the following 'warning' message will be logged: `"Can't find key(%s) in resourcebundle. Return: %s"`

```java
2019-04-19 22:36:34,708  WARN  Can't find key(not.existing.key.in.existing.resourcebundle) in resourcebundle. Return: <not.existing.key.in.existing.resourcebundle>     [DefaultI18NResourceBundle]
java.util.MissingResourceException: Can't find resource for bundle java.util.PropertyResourceBundle, key not.existing.key.in.existing.resourcebundle
	at java.util.ResourceBundle.getObject(ResourceBundle.java:450) ~[?:1.8.0_201]
	at java.util.ResourceBundle.getString(ResourceBundle.java:407) ~[?:1.8.0_201]
	at com.github.naoghuman.lib.i18n.internal.DefaultI18NResourceBundle.getMessage(DefaultI18NResourceBundle.java:96) [classes/:?]
	at com.github.naoghuman.lib.i18n.internal.DefaultI18NResourceBundleTest.getMessageWithResourceBundleThrowsMissingResourceException(DefaultI18NResourceBundleTest.java:110) [test-classes/:?]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:1.8.0_201]
        ...
```

```java
public final class DefaultI18NResourceBundle implements I18NResourceBundle {

    private static final String PATTERN_KEY_NAME = "<{0}>"; // NO18N
    ...
    @Override
    public String getMessage(final String key, final Object... arguments) {
        DefaultI18NValidator.requireNonNullAndNotEmpty(key);
        DefaultI18NValidator.requireNonNullAndNotEmpty(arguments);
        DefaultI18NValidator.requireResourceBundleExist(this.getBaseBundleName(), this.getActualLocale());
        
        final ResourceBundle bundle = getResourceBundle();
        String value = MessageFormat.format(PATTERN_KEY_NAME, key);
        
        if (bundle != null) {
            try {
                value = MessageFormat.format(bundle.getString(key), arguments);
            } catch (MissingResourceException mre) {
                LoggerFacade.getDefault().warn(this.getClass(), 
                        String.format("Can't find key(%s) in resourcebundle. Return: %s", key, value), 
                        mre);
            }
        }
        
        return value;
    }
    ...
}
```


### Convention: Defined supported Locales, default and actual Locale<a name="CoDeSuDeAcLo" />

_Supported Locales_
* Defines all supported [Locale]s in the momentary session.
* This array should reflectes all your defined languages .properties files.

```java
public final class I18NResourceBundleBuilder {
    ...
    private static final class I18NResourceBundleBuilderImpl implements
            FirstStep,  ForthStep, LastStep, SecondStep, ThirdStep
    {
        ...
        @Override
        public ThirdStep supportedLocales(final Locale... locales) {
            LoggerFacade.getDefault().debug(this.getClass(), "I18NResourceBundleBuilderImpl.supportedLocales(Locale...)"); // NOI18N
            
            final List<Locale> list = Arrays.asList(locales);
            DefaultI18NValidator.requireNonNullAndNotEmpty(list);
            
            final ObservableList<Locale> observableList = FXCollections.observableArrayList();
            observableList.addAll(list);
            properties.put(ATTR__SUPPORTED_LOCALES, new SimpleObjectProperty(observableList));
            
            return this;
        }

        @Override
        public ThirdStep supportedLocales(final ObservableList<Locale> locales) {
            LoggerFacade.getDefault().debug(this.getClass(), "I18NResourceBundleBuilderImpl.supportedLocales(ObservableList<Locale>)"); // NOI18N
            
            DefaultI18NValidator.requireNonNullAndNotEmpty(locales);
            properties.put(ATTR__SUPPORTED_LOCALES, new SimpleObjectProperty(locales));
            
            return this;
        }
        ...
    }
}
```

```java
public final class I18NFacade implements I18NBinding, I18NResourceBundle {
    ...
    @Override
    public ObservableList<Locale> getSupportedLocales() {
        return i18NResourceBundle.getSupportedLocales();
    }

    @Override
    public void setSupportedLocales(final ObservableList<Locale> locales) {
        i18NResourceBundle.setSupportedLocales(locales);
    }

    @Override
    public void setSupportedLocales(final Locale... locales) {
        i18NResourceBundle.setSupportedLocales(locales);
    }
    ...
}
```

```java
public final class DefaultI18NResourceBundle implements I18NResourceBundle {
    ...
    @Override
    public ObservableList<Locale> getSupportedLocales() {
        DefaultI18NValidator.requireNonNull(supportedLocales);
        
        return supportedLocales;
    }

    @Override
    public void setSupportedLocales(final ObservableList<Locale> locales) {
        DefaultI18NValidator.requireNonNullAndNotEmpty(locales);
        
        supportedLocales.clear();
        supportedLocales.addAll(locales);
    }

    @Override
    public void setSupportedLocales(final Locale... locales) {
        final List<Locale> list = Arrays.asList(locales);
        DefaultI18NValidator.requireNonNullAndNotEmpty(list);
        
        supportedLocales.clear();
        supportedLocales.addAll(list);
    }
    ...
}
```

_Default Locale_
* If the supported Locales doesn't contained the default Locale then the `Locale#ENGLISH` instead 
  will be return.

```java
public final class DefaultI18NResourceBundle implements I18NResourceBundle {
    ...
    @Override
    public void setDefaultLocale(final Locale locale) {
        DefaultI18NValidator.requireNonNull(locale);
        
        defaultLocale = this.getSupportedLocales().contains(locale) ? locale : Locale.ENGLISH;
    }
    ...
}
```

_Actual Locale_
* If the supported Locales doesn't contained the actual Locale then the `default` Locale instead 
  will be return.

```java

public final class DefaultI18NResourceBundle implements I18NResourceBundle {
    ...
    @Override
    public void setActualLocale(final Locale locale) {
        DefaultI18NValidator.requireNonNull(locale);
        
        actualLocaleProperty.set(this.getSupportedLocales().contains(locale) ? locale : defaultLocale);
    }
    ...
}
```


### Convention: Basic validation<a name="CoBaVa" />

* Every `functionality` in the builders and in the default implementations will checked 
  against minimal preconditions with the validator [DefaultI18NValidator].
* `Getters` and `Setters` functionalities are only checked if they are initial only 
  instantiate and not declarated.
* For example a String will be validate if it's not NULL and not EMPTY.

```java
public final class I18NResourceBundleBuilder {
    ...
    private static final class I18NResourceBundleBuilderImpl implements
            FirstStep,  ForthStep, LastStep, SecondStep, ThirdStep
    {
        ...
        @Override
        public SecondStep baseBundleName(final String baseBundleName) {
            LoggerFacade.getDefault().debug(this.getClass(), "I18NResourceBundleBuilderImpl.baseBundleName(String)"); // NOI18N

            DefaultI18NValidator.requireNonNullAndNotEmpty(baseBundleName);
            properties.put(ATTR__BASE_BUNDLE_NAME, new SimpleStringProperty(baseBundleName));

            return this;
        }
        ...
    }
}
```

```java
public final class DefaultI18NResourceBundle implements I18NResourceBundle {
    ...
    @Override
    public String getBaseBundleName() {
        DefaultI18NValidator.requireNonNullAndNotEmpty(baseBundleName);
        
        return baseBundleName;
    }

    @Override
    public void setBaseBundleName(final String baseBundleName) {
        DefaultI18NValidator.requireNonNullAndNotEmpty(baseBundleName);
        
        this.baseBundleName = baseBundleName;
    }
    ...
}
```

```java
public final class DefaultI18NBinding implements I18NBinding {
    ...
    @Override
    public StringBinding createStringBinding(final String key, Object... arguments) {
        DefaultI18NValidator.requireNonNullAndNotEmpty(key);
        DefaultI18NValidator.requireNonNullAndNotEmpty(arguments);
        
        return Bindings.createStringBinding(() -> I18NFacade.getDefault().getMessage(key, arguments), I18NFacade.getDefault().actualLocaleProperty());
    }
    ...
}
```



Examples<a name="Examples" />
---

### How to use the builder I18NResourceBundleBuilder<a name="HoToUsReBuBu" />

With the builder [I18NResourceBundleBuilder] the developer can configure the 
[ResourceBundle] which contains the `key - value` terms which will then be bind 
to a [Locale]. That means switching the `actual` Locale update all binded textes 
with the specific values from the corresponding language `.properties` file.

#### Specification: _Usage of I18NResourceBundleBuilder_
```java
/**
 * 1) Starts the configuration process.
 * 2) Defines the path and name from the .properties file.
 * 3) Sets all supported Locales with an [].
 * 4) Sets all supported Locales with an ObservableList.
 * 5) Sets the default Locale.
 * 6) Sets the actual Locale.
 * 7) Completes the configuration process.
 */
I18NResourceBundleBuilder.configure() // 1
        .baseBundleName(String)       // 2
        .supportedLocales(Locale...)  // 3
        .supportedLocales(ObservableList<Locale>) // 4
        .defaultLocale(Locale)        // 5
        .actualLocale(Locale)         // 6
        .build();                     // 7
```

#### Examples:
```java
@Test
public void lastStepWithSupportedLocalesAsArray() {
    String resourcbundle = "com.github.naoghuman.lib.i18n.internal.resourcebundle";
    I18NResourceBundleBuilder.configure()
            .baseBundleName(resourcbundle)
            .supportedLocales(Locale.ITALIAN, Locale.JAPANESE)
            .defaultLocale(Locale.ITALIAN)
            .actualLocale(Locale.JAPANESE)
            .build();

    assertEquals(resourcbundle,   I18NFacade.getDefault().getBaseBundleName());
    assertEquals(Locale.ITALIAN,  I18NFacade.getDefault().getDefaultLocale());
    assertEquals(Locale.JAPANESE, I18NFacade.getDefault().getActualLocale());
    assertEquals(2,               I18NFacade.getDefault().getSupportedLocales().size());
}

@Test
public void lastStepWithSupportedLocalesAsObservableList() {
    String resourcbundle = "com.github.naoghuman.lib.i18n.internal.resourcebundle";
    final ObservableList<Locale> locales = FXCollections.observableArrayList();
    locales.addAll(Locale.ITALIAN, Locale.JAPANESE, Locale.FRENCH);
    I18NResourceBundleBuilder.configure()
            .baseBundleName(resourcbundle)
            .supportedLocales(locales)
            .defaultLocale(Locale.ITALIAN)
            .actualLocale(Locale.JAPANESE)
            .build();

    assertEquals(resourcbundle,  I18NFacade.getDefault().getBaseBundleName());
    assertEquals(Locale.ITALIAN, I18NFacade.getDefault().getDefaultLocale());
    assertEquals(Locale.JAPANESE,I18NFacade.getDefault().getActualLocale());
    assertEquals(3,              I18NFacade.getDefault().getSupportedLocales().size());
}
```

### How to use the builder I18NBindingBuilder<a name="HoToUsBiBu" />

The builder [I18NBindingBuilder] let the developer create a [StringBinding]. The 
StringBinding can created with a function from type [Callable&lt;String&gt;] or 
with a .properties `key` and optional `arguments`.

#### Specification: _Usage of I18NBindingBuilder_
```java
/**
 * 1) Starts the binding process.
 * 2) Use the given function to create a StringBinding.
 * 3) Completes the binding process and returns the StringBinding.
 */
I18NBindingBuilder.bind()          // 1
       .callable(Callable<String>) // 2
       .build();                   // 3

/**
 * 1) Starts the binding process.
 * 2) Defines the key which value will be bind to the StringBinding.
 * 3) Optional arguments for the value from the given key.
 * 4) Completes the binding process and returns the StringBinding.
 */
I18NBindingBuilder.bind()         // 1
       .key(String)               // 2
       .arguments(Object... args) // 3
       .build();                  // 4
```

#### Examples:
```java
@Test
public void lastStepCallable() {
    I18NResourceBundleBuilder.configure()
            .baseBundleName("com.github.naoghuman.lib.i18n.internal.resourcebundle")
            .supportedLocales(Locale.ENGLISH, Locale.GERMAN)
            .defaultLocale(Locale.ENGLISH)
            .actualLocale(Locale.GERMAN)
            .build();

    Optional<StringBinding> result = I18NBindingBuilder.bind()
            .callable(() -> I18NMessageBuilder.message()
                    .key("resourcebundle.title")
                    .build()
            )
            .build();
    assertTrue(result.isPresent());
    assertEquals("RB: Test Titel", result.get().get());

    I18NFacade.getDefault().setActualLocale(Locale.ENGLISH); // Here the magic happen :)
    assertEquals("RB: Test title", result.get().get());
}

@Test
public void lastStepKeyWithoutArguments() {
    I18NResourceBundleBuilder.configure()
            .baseBundleName("com.github.naoghuman.lib.i18n.internal.resourcebundle")
            .supportedLocales(Locale.ENGLISH, Locale.GERMAN)
            .defaultLocale(Locale.ENGLISH)
            .actualLocale(Locale.GERMAN)
            .build();

    Optional<StringBinding> result = I18NBindingBuilder.bind()
            .key("resourcebundle.title")
            .build();
    assertTrue(result.isPresent());
    assertEquals("RB: Test Titel", result.get().get());

    I18NFacade.getDefault().setActualLocale(Locale.ENGLISH); // Here the magic happen :)
    assertEquals("RB: Test title", result.get().get());
}

@Test
public void lastStepKeyWithArguments() {
    I18NResourceBundleBuilder.configure()
            .baseBundleName("com.github.naoghuman.lib.i18n.internal.resourcebundle")
            .supportedLocales(Locale.ENGLISH, Locale.GERMAN)
            .defaultLocale(Locale.ENGLISH)
            .actualLocale(Locale.GERMAN)
            .build();

    Optional<StringBinding> result = I18NBindingBuilder.bind()
            .key("resourcebundle.label.with.parameter")
            .arguments(123)
            .build();
    assertTrue(result.isPresent());
    assertEquals("RB: Text mit Parameter: 123", result.get().get());

    I18NFacade.getDefault().setActualLocale(Locale.ENGLISH); // Here the magic happen :)
    assertEquals("RB: Text with parameter: 123", result.get().get());
}
```

### How to use the builder I18NMessageBuilder<a name="HoToUsMeBu" />

To load a .properties `key` with optional `arguments` from the initialized [ResourceBundle] 
through the [I18NResourceBundleBuilder] the developer can use the builder [I18NMessageBuilder].

#### Specification: _Usage of I18NMessageBuilder_
```java
/**
 * 1) Starts the message process.
 * 2) Defines the key which value will be loaded.
 * 3) Optional arguments for the value from the given key.
 * 4) Completes the message process and returns a String.
 */
I18NMessageBuilder.message()  // 1
        .key(String)          // 2
        .arguments(Object...) // 3
        .build();             // 4
```

#### Examples:
```java
@Test
public void lastStepWithoutArguments() {
    I18NResourceBundleBuilder.configure()
            .baseBundleName("com.github.naoghuman.lib.i18n.internal.resourcebundle")
            .supportedLocales(Locale.ENGLISH, Locale.GERMAN)
            .defaultLocale(Locale.ENGLISH)
            .actualLocale(Locale.GERMAN)
            .build();

    String result = I18NMessageBuilder.message()
            .key("resourcebundle.title")
            .build();
    assertEquals("RB: Test Titel", result);

    I18NFacade.getDefault().setActualLocale(Locale.ENGLISH); // Here the magic happen :)
    result = I18NMessageBuilder.message()
            .key("resourcebundle.title")
            .build();
    assertEquals("RB: Test title", result);
}
    
@Test
public void lastStepWithArguments() {
    I18NResourceBundleBuilder.configure()
            .baseBundleName("com.github.naoghuman.lib.i18n.internal.resourcebundle")
            .supportedLocales(Locale.ENGLISH, Locale.GERMAN)
            .defaultLocale(Locale.ENGLISH)
            .actualLocale(Locale.GERMAN)
            .build();

    String result = I18NMessageBuilder.message()
            .key("resourcebundle.label.with.parameter")
            .arguments(2)
            .build();
    assertEquals("RB: Text mit Parameter: 2", result);

    I18NFacade.getDefault().setActualLocale(Locale.ENGLISH); // Here the magic happen :)
    result = I18NMessageBuilder.message()
            .key("resourcebundle.label.with.parameter")
            .arguments(123)
            .build();
    assertEquals("RB: Text with parameter: 123", result);
}
```



Demo<a name="Demo" />
---

The demo applications shows how to integrate the library `Lib-I18N` in four simple steps.

_Image:_ Demo application
![Lib-I18N_Demo-English_v0.8.0_2019-04-27_16-14.png][Lib-I18N_Demo-English_v0.8.0_2019-04-27_16-14]

__Hint:__  
To run the demo local the library `Lib-FMXL v0.3.0-PRERELEASE` is needed which isn't 
momentary available in `Maven Central`. The library can be downloaded manually here: 
https://github.com/Naoghuman/lib-fxml

### Step one: Prepare your application<a name="DePrYoAp" />

First inject the library 'Lib-I18N' into your project.  
Then create for every supported language a .properties file.

```xml
<dependencies>
    <dependency>
        <groupId>com.github.naoghuman</groupId>
        <artifactId>lib-i18n</artifactId>
        <version>0.8.0</version>
    </dependency>
</dependencies>
```

_Image:_ Demo .properties files 
![Lib-I18N_Demo-properties-files_v0.8.0_2019-04-27_17-33.png][Lib-I18N_Demo-properties-files_v0.8.0_2019-04-27_17-33]


### Step two: Register the ResourceBundle<a name="DeStTw" />

Next register the ResourceBundle where `supportedLocales` are corresponding to every existing .properties file and `actualLocale` is the language which will shown first.

```java
public final class DemoI18NStart extends Application {

    @Override
    public void init() throws Exception {
        I18NResourceBundleBuilder.configure()
                .baseBundleName("com.github.naoghuman.lib.i18n.demo_i18n") // NOI18N
                .supportedLocales(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN)
                .defaultLocale(Locale.ENGLISH)
                .actualLocale(Locale.ENGLISH)
                .build();
    }
}
```

### Step three: Bind the text components<a name="DeStTh" />

In the third step the text components will be bind to the depending key from the ResourceBundle.

```java
public final class DemoI18NController extends FXMLController implements Initializable {

    @FXML private Button bFrench;
    @FXML private Button bGerman;
    @FXML private Button bItalian;
    @FXML private Button bEnglish;
    @FXML private Label lLanguages;
    @FXML private Text tAbout;
    @FXML private Text tFrom;
    @FXML private Text tHello;
    @FXML private Text tLand;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        // Menu
        this.bind(lLanguages.textProperty(), "demo.i18n.languages");        // NOI18N
        this.bind(bFrench.textProperty(),    "demo.i18n.language.french");  // NOI18N
        this.bind(bGerman.textProperty(),    "demo.i18n.language.german");  // NOI18N
        this.bind(bItalian.textProperty(),   "demo.i18n.language.italian"); // NOI18N
        this.bind(bEnglish.textProperty(),   "demo.i18n.language.english"); // NOI18N
        
        // Message
        this.bind(tHello.textProperty(), "demo.i18n.greetings"); // NOI18N
        this.bind(tFrom.textProperty(),  "demo.i18n.from");      // NOI18N
        this.bind(tLand.textProperty(),  "demo.i18n.land");      // NOI18N
        this.bind(tAbout.textProperty(), "demo.i18n.about");     // NOI18N
    }

    private void bind(final StringProperty stringProperty, final String key) {
        final Optional<StringBinding> optionalStringBinding = I18NBindingBuilder.bind().key(key).build();
        optionalStringBinding.ifPresent(stringBinding -> {
            stringProperty.bind(stringBinding);
        });
    }
}
```

### Step four: Switch the language during runtime<a name="DeStFo" />

And in the last step the user will change the language in the runing application which leads to a change from the `actualLocale` which performs then the language update in the gui.

```java
public final class DemoI18NController extends FXMLController implements Initializable {

    public void onActionSwitchToLanguageFrench() {
        if (I18NFacade.getDefault().getActualLocale().equals(Locale.FRENCH)) {
            LoggerFacade.getDefault().debug(this.getClass(), "Shows already the Locale.FRENCH - do nothing."); // NOI18N
            return;
        }
        
        I18NFacade.getDefault().setActualLocale(Locale.FRENCH);
    }
    
    public void onActionSwitchToLanguageGerman() {
        if (I18NFacade.getDefault().getActualLocale().equals(Locale.GERMAN)) {
            LoggerFacade.getDefault().debug(this.getClass(), "Shows already the Locale.GERMAN - do nothing."); // NOI18N
            return;
        }
        
        I18NFacade.getDefault().setActualLocale(Locale.GERMAN);
    }
    
    ...
}
```



JavaDoc<a name="JavaDoc" />
---

The [JavaDoc] from the library `Lib-I18N` can be explored here: [JavaDoc Lib-I18N]

_Image:_ JavaDoc Lib-I18N v0.7.2  
![Lib-I18N_JavaDoc_v0.7.2_2019-04-22_09-39.png][Lib-I18N_JavaDoc_v0.7.2_2019-04-22_09-39]



Download<a name="Download" />
---

Current `version` is `0.8.0`. Main points in this release are:
* Implement a demo which shows how easy an application becomes multilingual in 
  four steps :smile: .  
* Extend the ReadMe with a new chapter 'Demo'.
* Fix some minor bugs (test files are copied into the .jar file).

**Maven coordinates**  
In context from a [Maven] project you can use following maven coordinates: 
```xml
<dependencies>
    <dependency>
        <groupId>com.github.naoghuman</groupId>
        <artifactId>lib-i18n</artifactId>
        <version>0.8.0</version>
    </dependency>

    <!-- optional -->
    <dependency>
        <groupId>com.github.naoghuman</groupId>
        <artifactId>lib-logger</artifactId>
        <version>0.6.0</version>
    </dependency>
</dependencies>
```

Download:
* [Release v0.8.0] (04.28.2019 / MM.dd.yyyy)

An overview about all existings releases can be found here:
* [Overview] from all releases in `Lib-I18N`.



Requirements<a name="Requirements" />
---

* On your system you need [JRE 8] or [JDK 8] installed.
* The library [lib-i18n-0.8.0.jar](#Installation).

In the library are following libraries registered as dependencies:
* The library [lib-logger-0.6.0.jar](#Installation).
    - Included in `Lib-Logger` is the library [log4j-api-2.10.0.jar].
    - Included is `Lib-Logger` is the library [log4j-core-2.10.0.jar].



Installation<a name="Installation" />
---

##### Install the project in your preferred IDE

* If not installed download the [JRE 8] or the [JDK 8].
    - Optional: To work better with [FXML] files in a [JavaFX] application 
      download the JavaFX [Scene Builder] supported by `Gluon`.
* Choose your preferred IDE (e.g. [NetBeans], [Eclipse] or [IntelliJ IDEA]) for development.
* Download or clone [Lib-Logger].
* Open the projects in your IDE and run them.



Contribution<a name="Contribution" />
---

* If you find a `Bug` I will be glad if you could report an [Issue].
* If you want to contribute to the project plz fork the project and do a [Pull Request].



License<a name="License" />
---

The project `Lib-I18n` is licensed under [General Public License 3.0].



Autor<a name="Autor" />
---

The project `Lib-I18n` is maintained by me, Peter Rogge. See [Contact](#Contact).



Contact<a name="Contact" />
---

You can reach me under <peter.rogge@yahoo.de>.



[//]: # (Images)
[Lib-I18N_Demo_v0.8.0_2019-04-27_16-24]:https://user-images.githubusercontent.com/8161815/56850906-1a659d80-6909-11e9-93f2-a1b099875c2f.png
[Lib-I18N_Demo-English_v0.8.0_2019-04-27_16-14]:https://user-images.githubusercontent.com/8161815/56850913-29e4e680-6909-11e9-9a87-f519c9b9bf71.png
[Lib-I18N_Demo-properties-files_v0.8.0_2019-04-27_17-33]:https://user-images.githubusercontent.com/8161815/56851721-b1832300-6912-11e9-9638-bc95ca416c60.png
[Lib-I18N_JavaDoc_v0.7.2_2019-04-22_09-39]:https://user-images.githubusercontent.com/8161815/56489671-beeb7800-64e2-11e9-8553-803fc8d15dc8.png



[//]: # (Links)
[App-I18N-Demo]:https://github.com/Naoghuman/app-i18n-demo
[Callable&lt;String&gt;]:https://docs.oracle.com/javase/8/docs/api/index.html?java/util/concurrent/Callable.html
[DefaultI18NValidator]:https://github.com/Naoghuman/lib-i18n/blob/master/src/main/java/com/github/naoghuman/lib/i18n/internal/DefaultI18NValidator.java
[Eclipse]:https://www.eclipse.org/
[FXML]:http://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm
[General Public License 3.0]:http://www.gnu.org/licenses/gpl-3.0.en.html
[I18NBindingBuilder]:https://github.com/Naoghuman/lib-i18n/blob/master/src/main/java/com/github/naoghuman/lib/i18n/core/I18NBindingBuilder.java
[I18NMessageBuilder]:https://github.com/Naoghuman/lib-i18n/blob/master/src/main/java/com/github/naoghuman/lib/i18n/core/I18NMessageBuilder.java
[I18NResourceBundleBuilder]:https://github.com/Naoghuman/lib-i18n/blob/master/src/main/java/com/github/naoghuman/lib/i18n/core/I18NResourceBundleBuilder.java
[IntelliJ IDEA]:http://www.jetbrains.com/idea/
[Issue]:https://github.com/Naoghuman/lib-i18n/issues
[JavaDoc]:http://www.oracle.com/technetwork/java/javase/documentation/index-jsp-135444.html
[JavaDoc Lib-I18N]:http://naoghuman.github.io/lib-i18n/apidocs
[JavaFX]:http://docs.oracle.com/javase/8/javase-clienttechnologies.htm
[JDK 8]:http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[JRE 8]:http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
[Lib-Logger]:https://github.com/Naoghuman/lib-logger
[Locale]:https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html
[log4j-api-2.10.0.jar]:https://logging.apache.org/log4j/2.0/log4j-web/dependencies.html
[log4j-core-2.10.0.jar]:https://logging.apache.org/log4j/2.0/log4j-web/dependencies.html
[Maven]:http://maven.apache.org/
[Maven Central]:https://search.maven.org/
[NetBeans]:https://netbeans.org/
[Overview]:https://github.com/Naoghuman/lib-i18n/releases
[Pull Request]:https://help.github.com/articles/using-pull-requests
[Release v0.8.0]:https://github.com/Naoghuman/lib-i18n/releases/tag/v0.8.0
[MissingResourceException]:https://docs.oracle.com/javase/8/docs/api/java/util/MissingResourceException.html
[ResourceBundle]:https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html
[Scene Builder]:https://gluonhq.com/products/scene-builder/
[StringBinding]:https://docs.oracle.com/javase/8/javafx/api/javafx/beans/binding/StringBinding.html
[Travis CI]:https://travis-ci.org/
[UML]:https://en.wikipedia.org/wiki/Unified_Modeling_Language
