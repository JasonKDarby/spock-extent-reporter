package com.jdarb.spockExtentReporter

import org.spockframework.runtime.ConditionNotSatisfiedError
import spock.lang.Specification
import spock.util.EmbeddedSpecRunner

class SpockExtentReporterExtensionSpec extends Specification {

  def 'something'() {
    given: 'a spec runner with an extent report extension'
    EmbeddedSpecRunner runner = new EmbeddedSpecRunner()
    runner.extensionClasses << SpockExtentReporterExtension.class

    and: 'a demo specification'
    String specification = new File(getClass().getResource(specFileName).toURI()).text

    and: 'system properties set properly'
    System.setProperty('REPLACE_EXISTING_REPORT', 'false')
    System.setProperty('WRITE_FEATURE_BLOCK_LABELS', writeFeatureBlockLabels)

    when: 'the spec runs'
    runner.run specification

    then: 'something'
    1 == 1

    where:
    specFileName                  | writeFeatureBlockLabels
    '/BasicFooSpec.groovy'        | 'false'
    '/LabeledFooSpec.groovy'      | 'true'
  }

  def 'run failing specifications'() {
    given: 'a spec runner with an extent report extension'
    EmbeddedSpecRunner runner = new EmbeddedSpecRunner()
    runner.extensionClasses << SpockExtentReporterExtension.class

    and: 'a failing demo specification'
    String specification = new File(getClass().getResource(specFileName).toURI()).text

    and: 'system properties set properly'
    System.setProperty('REPLACE_EXISTING_REPORT', 'false')
    System.setProperty('WRITE_FEATURE_BLOCK_LABELS', 'false')

    when: 'the spec runs'
    runner.run specification

    then: 'an error is thrown'
    thrown ConditionNotSatisfiedError

    and: 'the report is as expected'

    where:
    specFileName                  | expected
    '/FailingBasicFooSpec.groovy' | 'welp'
  }

  def 'run an ignored specification'() {
    given: 'a spec runner with an extent report extension'
    EmbeddedSpecRunner runner = new EmbeddedSpecRunner()
    runner.extensionClasses << SpockExtentReporterExtension.class

    and: 'an ignored demo specification'
    String specification = new File(getClass().getResource(specFileName).toURI()).text

    and: 'system properties set properly'
    System.setProperty('REPLACE_EXISTING_REPORT', 'false')
    System.setProperty('WRITE_FEATURE_BLOCK_LABELS', 'false')

    when: 'the spec runs'
    runner.run specification

    then: 'something'
    1 == 1

    where:
    specFileName              | expected
    '/SkippedFooSpec.groovy'  | 'welp'
  }

  def 'run a specification with ignored features'() {
    given: 'a spec runner with an extent report extension'
    EmbeddedSpecRunner runner = new EmbeddedSpecRunner()
    runner.extensionClasses << SpockExtentReporterExtension.class

    and: 'an ignored demo specification'
    String specification = new File(getClass().getResource(specFileName).toURI()).text

    and: 'system properties set properly'
    System.setProperty('REPLACE_EXISTING_REPORT', 'false')
    System.setProperty('WRITE_FEATURE_BLOCK_LABELS', 'false')

    when: 'the spec runs'
    runner.run specification

    then: 'something'
    1 == 1

    where:
    specFileName                      | expected
    '/SkippedFeaturesFooSpec.groovy'  | 'welp'
  }

}
