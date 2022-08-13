pipeline {
  agent any
  stages {
    stage('sleep') {
      agent any
      steps {
        sleep 10
      }
    }

    stage('print') {
      steps {
        echo 'hello world'
      }
    }

  }
}