package me.soulmachine;

import org.jruby.embed.ScriptingContainer;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 * A simple JRuby example to execute Python scripts from Java.
 */
final class JRubyExample {
  private JRubyExample() {}

  /**
   * Main entrypoint.
   *
   * @param args arguments
   * @throws ScriptException ScriptException
   */
  public static void main(final String[] args) throws ScriptException {
    listEngines();

    final String rubyHelloWord = "puts 'Hello World from JRuby!'";

    // First way: Use built-in ScriptEngine from JDK
    {
      final ScriptEngineManager mgr = new ScriptEngineManager();
      final ScriptEngine pyEngine = mgr.getEngineByName("ruby");

      try {
        pyEngine.eval(rubyHelloWord);
      } catch (ScriptException ex) {
        ex.printStackTrace();
      }
    }

    // Second way: Use ScriptingContainer() from JRuby
    {
      final ScriptingContainer scriptingContainer = new ScriptingContainer();
      scriptingContainer.runScriptlet(rubyHelloWord);
    }

    // Call Ruby Methods from Java
    {
      final ScriptingContainer scriptingContainer = new ScriptingContainer();
      final String rubyMethod = "def myAdd(a,b)\n\treturn a+b\nend";
      final Object receiver = scriptingContainer.runScriptlet(rubyMethod);
      final Object[] arguments = new Object[2];
      arguments[0] = Integer.valueOf(6);
      arguments[1] = Integer.valueOf(4);
      final Integer result = scriptingContainer.callMethod(receiver, "myAdd",
          arguments, Integer.class);
      System.out.println("Result: " + result);
    }
  }

  /**
   * Display all script engines.
   */
  public static void listEngines() {
    final ScriptEngineManager mgr = new ScriptEngineManager();
    final List<ScriptEngineFactory> factories =
        mgr.getEngineFactories();
    for (final ScriptEngineFactory factory: factories) {
      System.out.println("ScriptEngineFactory Info");

      final String engName = factory.getEngineName();
      final String engVersion = factory.getEngineVersion();
      final String langName = factory.getLanguageName();
      final String langVersion = factory.getLanguageVersion();

      System.out.printf("\tScript Engine: %s (%s)\n", engName, engVersion);

      final List<String> engNames = factory.getNames();
      for (final String name: engNames) {
        System.out.printf("\tEngine Alias: %s\n", name);
      }
      System.out.printf("\tLanguage: %s (%s)\n", langName, langVersion);
    }
  }
}
