package com.jdarb.spockExtentReporter.listeners

import com.relevantcodes.extentreports.ExtentReports
import com.relevantcodes.extentreports.ExtentTest
import com.relevantcodes.extentreports.LogStatus
import com.typesafe.config.Config
import groovy.transform.CompileStatic
import org.spockframework.runtime.IRunListener
import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.IterationInfo
import org.spockframework.runtime.model.SpecInfo

@CompileStatic
class SpecInfoListener implements IRunListener {

  ExtentReports extentReport
  ExtentTest specificationTest
  Map<String, ExtentTest> featureTests = [:]
  String currentTestName
  Config config

  SpecInfoListener(ExtentReports extentReport, Config config) {
    this.extentReport = extentReport
    this.config = config
  }

  @Override
  void beforeSpec(SpecInfo spec) {
    specificationTest = extentReport.startTest spec.name
  }

  @Override
  void beforeFeature(FeatureInfo feature) {

  }

  @Override
  void beforeIteration(IterationInfo iteration) {
    String testName = deriveTestName(iteration)
    featureTests[testName] = extentReport.startTest testName
    if(config.getBoolean('write-feature-block-labels')) {
      featureTests[testName].log(LogStatus.INFO, formatFeatureBlockLabels(iteration.feature))
    }
    currentTestName = testName
  }

  @Override
  void afterIteration(IterationInfo iteration) {
    if(featureTests[deriveTestName(iteration)].runStatus != LogStatus.ERROR) {
      featureTests[deriveTestName(iteration)].log(LogStatus.PASS, 'PASS')
    } else {
      featureTests[deriveTestName(iteration)].log(LogStatus.FAIL, 'FAIL')
    }

    extentReport.endTest featureTests[deriveTestName(iteration)]
    specificationTest.appendChild featureTests[deriveTestName(iteration)]
  }

  @Override
  void afterFeature(FeatureInfo feature) {

  }

  @Override
  void afterSpec(SpecInfo spec) {
    extentReport.endTest specificationTest
  }

  @Override
  void error(ErrorInfo error) {
    featureTests[currentTestName].log(LogStatus.ERROR, error.exception)
  }

  @Override
  void specSkipped(SpecInfo spec) {
    skipTest spec.name
  }

  @Override
  void featureSkipped(FeatureInfo feature) {
    specificationTest.appendChild(skipTest(feature.name))
  }

  ExtentTest skipTest(String testName) {
    ExtentTest skippedTest = extentReport.startTest testName
    skippedTest.log(LogStatus.SKIP, 'SKIPPED')
    extentReport.endTest skippedTest
    skippedTest
  }

  static String deriveTestName(IterationInfo iteration) {
    String testName = iteration.name
    if(iteration.dataValues.length > 0) {
      List<String> dataValues = []
      (iteration.dataValues.length).times { int i ->
        dataValues << iteration.dataValues[i].toString()
      }
      testName = "$testName ${ dataValues }"
    }
    testName
  }

  static String formatFeatureBlockLabels(FeatureInfo feature) {
    feature.blocks.collect {
      "${it.kind.name()} ${it.texts.join('|')}"
    }.inject('') { i, j ->
      "$i<p>$j</p>"
    }
  }
}
