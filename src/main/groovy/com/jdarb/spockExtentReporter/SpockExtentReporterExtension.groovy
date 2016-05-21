package com.jdarb.spockExtentReporter

import com.jdarb.spockExtentReporter.listeners.SpecInfoListener
import com.relevantcodes.extentreports.ExtentReports
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import groovy.transform.CompileStatic
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.SpecInfo

@CompileStatic
class SpockExtentReporterExtension implements IGlobalExtension {

  final Config config = ConfigFactory.load()
  final ExtentReports extentReport =
      new ExtentReports(config.getString('report-path'), config.getBoolean('replace-existing-report'))

  @Override
  void start() {
    //Flush writes SystemInfo to report
    extentReport.flush()
  }

  @Override
  void visitSpec(SpecInfo spec) {
    //I'm really don't think this is ultimately desirable but it's used to enable config changes during testing
    ConfigFactory.invalidateCaches()
    spec.addListener new SpecInfoListener(extentReport, ConfigFactory.load())
  }

  @Override
  void stop() {
    extentReport.close()
  }
}
