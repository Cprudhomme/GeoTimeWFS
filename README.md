https://geotimewfs.herokuapp.com/

# GeoTimeWFS
The goal of this project is to enrich geospatial data with linked data so that metadata and temporal revisions are no longer collected by record, but by attribute/triple in the knowledge base. This requires the development of a metadata and time concept that requires a triple declaration of timeliness, quality and origin of the data and that must be dynamically pursued during editing. The second objective of this project is a data suggestion system to enrich the knowledge base in order to facilitate the work of managing geospatial data. An intelligent solution must be developed for this and enriched maps must be displayed and generated dynamically.

## Ontology mapping:
https://www.w3.org/2015/spatial/wiki/ISO_19115_-_DCAT_-_Schema.org_mapping


## Deploy local dep
mvn deploy:deploy-file -Durl=file:./repo/ -Dfile=libs/pisemantic-1.0-SNAPSHOT.jar -DgroupId=info.ponciano.lab -DartifactId=pisemantic -Dpackaging=jar -Dversion=1.0

or install:

mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile="libs/pisemantic-1.0-SNAPSHOT.jar"  package