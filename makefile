# Makefile to aid running Node and testing

# Variables
PROJECT_NAME := ep-dsid
MAVEN := mvn

# Targets
.PHONY: all clean

all: jar run

jar:
	@echo "Creating JAR file for $(PROJECT_NAME)..."
	@mvn clean package
