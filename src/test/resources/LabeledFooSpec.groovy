import spock.lang.Specification

class LabeledFooSpec extends Specification {

  def 'a labeled feature'() {
    given: 'an input variable'
    def input = 0

    when: 'the input is modified'
    input += 1

    then: 'the modification is reflected'
    input == 1
  }

  def 'a labeled parametrized feature'() {
    when: 'the input is modified'
    input += 1

    then: 'the modification is reflected'
    input == expected

    where:
    input | expected
    0     | 1
    1     | 2
    2     | 3
  }

}