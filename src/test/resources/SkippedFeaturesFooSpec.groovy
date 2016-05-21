import spock.lang.Ignore
import spock.lang.Specification

class SkippedFeaturesFooSpec extends Specification {

  def 'a dummy feature'() {
    expect:
    1 == 1
  }

  @Ignore
  def 'a skipped feature'() {
    expect:
    1 == 1
  }

  @Ignore
  def 'a skipped parametrized feature'() {
    expect:
    input == output

    where:
    input | output
    0     | 0
    1     | 1
    2     | 2
  }

}