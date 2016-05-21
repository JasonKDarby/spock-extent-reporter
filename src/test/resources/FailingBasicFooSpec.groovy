import spock.lang.Specification

class FailingBasicFooSpec extends Specification {

  def 'a failing basic feature'() {
    expect:
    1 != 1
  }

  def 'a failing parametrized feature'() {
    expect:
    input == expected

    where:
    input | expected
    1     | 0
    2     | 1
    3     | 2
  }

}