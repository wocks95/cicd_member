Jenkins Pipeline 프로젝트

1. 의미
지속적 배포(CD: Continuous Delivery) 파이프라인을 Jenkins에 구현하고 통합하는 것을 지원하는 플러그인의 모음을 의미합니다.

2. Scripted Pipeline syntax
  1) Groovy 스크립트를 이용하여 파이프라인을 구성합니다.
  2) JAVA API를 사용할 수 있어 다양한 워크플로우 구성 및 커스터마이징이 가능합니다.
  3) 모든 유효한 선언적 파이프라인은 pipeline 블록으로 묶여야 합니다.

3. 기본 파이프라인 블록
pipeline {
    /* Declare Pipeline here */
}

4. Pipeline's syntax
  1) 문장 구분자인 세미콜론을 사용하지 않습니다. 각 문장은 하나씩 라인을 차지해야 합니다.
  2) 기본 파이프라인 블록은 Sections, Directives, Steps 등으로 구성됩니다.
  3) Section
    (1) 하나 이상의 Directive 또는 Step을 포함하는 논리적인 그룹입니다.
  4) Directive
    (1) 실제 기능이 수행되기 위한 환경이나 도구를 지정합니다.
    (2) environment, options, parameters, triggers, stage, tools, input 등으로 구성됩니다.
    (3) https://www.jenkins.io/doc/book/pipeline/syntax/#declarative-directives
  5) Step
    (1) 실제 Jenkins가 수행할 작업을 의미합니다.
    (2) 각 Step을 수행하는 플러그인에서 해당 기능을 제공합니다.
    (3) 파이프라인 Step 단계는 agent 및 workspace를 잠금(Locking)하고 난 뒤, 외부 프로세스를 실행합니다.
    (4) https://www.jenkins.io/doc/pipeline/steps/
  6) 문자열 보간(String interpolation)
    (1) Jenkins Pipeline은 문자열 보간을 위해 Groovy와 동일한 규칙을 사용합니다.
    (2) 작은 따옴표('') 또는 큰 따옴표("")를 이용해 문자열을 선언합니다.
    (3) 큰 따옴표("")로 선언한 문자열만 문자열 보간을 지원합니다.
    (4) 예시
      def name = 'Min'
      echo 'Hello ${name}'  ->  결과  Hello ${name}
      echo "Hello ${name}"  ->  결과  Hello Min