pipeline {

	agent {
		label 'main'
	}
	
	environment {
		
		ARTIFACT_ID = readMavenPom().getArtifactId()
    	VERSION = readMavenPom().getVersion()
    	DOCKER_REPO = "ghcr.io/kevin-bowersox-courses/"
    	DOCKER_REPO_PATTERN = "ghcr.io\\/kevin-bowersox-courses\\/"
    	JAR_FILE = "${ARTIFACT_ID}-${BUILD_NUMBER}.jar"
    	
	}
	
    stages {
        
        stage('Unit Tests') { 
            agent {
        		docker {
            		image 'maven:3.6.3-openjdk-11-slim' 
            		args '-v /home/vagrant/.m2:/root/.m2'
            		reuseNode true
        		}
    		}
            steps {
                sh 'mvn clean test' 
            }
        }
        
        stage('Build') { 
        
            agent {
        		docker {
            		image 'maven:3.6.3-openjdk-11-slim' 
            		args '-v /home/vagrant/.m2:/root/.m2'
            		reuseNode true 
        		}
    		}
    		
            steps {
                sh 'mvn -B -DskipTests -DjarName=${ARTIFACT_ID}-${BUILD_NUMBER} clean package' 
            }
            
        }
        
       	stage('Build Image') { 
	
			environment{
				JAR_FILE_LOCATION = "target/${ARTIFACT_ID}-${BUILD_NUMBER}.jar"
				IMAGE_ID = "${ARTIFACT_ID}:${BUILD_NUMBER}"
			}
			
            steps {
				
				script{
					docker.withRegistry('https://ghcr.io/kevin-bowersox-courses/', 'github-pat'){			
						sh 'docker build --build-arg JAR_FILE=${JAR_FILE_LOCATION} -t ${IMAGE_ID} .'
						sh 'docker tag ${IMAGE_ID} ${DOCKER_REPO}${ARTIFACT_ID}:${BUILD_NUMBER}'  
						sh 'docker push ${DOCKER_REPO}${ARTIFACT_ID}:${BUILD_NUMBER}'  
					}
            	}
            }
        }
        
        stage('Update Deploy Repo') { 
	
            steps {
                git url: "git@github.com:Kevin-Bowersox-Courses/javacd-deploy.git",
                	credentialsId: '33781024-0c1a-4626-9cf8-fc11342c75cd',
                	branch: 'master'
                sh 'git config --global user.email "kmb385@gmail.com"'
  				sh 'git config --global user.name "Kevin Bowersox"'
  				sh  "echo 's;image: ${DOCKER_REPO_PATTERN}\${ARTIFACT_ID}:[0-9]*;image: ${DOCKER_REPO}${ARTIFACT_ID}:${BUILD_NUMBER};g'"
                sh  "find . -type f -exec sed -i 's;image: ${DOCKER_REPO_PATTERN}${ARTIFACT_ID}:[0-9]*;image: ${DOCKER_REPO}${ARTIFACT_ID}:${BUILD_NUMBER};g' {} +"
                sh 'git commit -am "Updated image label to ${BUILD_NUMBER}"'
                sshagent(['33781024-0c1a-4626-9cf8-fc11342c75cd']) {
  					sh "git push origin master"
				}
            }
        }
    }
    post { 
		always { 
			cleanWs()
		}
	}
}