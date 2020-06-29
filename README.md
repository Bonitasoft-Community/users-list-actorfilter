# Users List Actor Filter

![](https://github.com/Bonitasoft-Community/users-list-actorfilter/workflows/Build/badge.svg)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=Bonitasoft-Community_users-list-actorfilter&metric=alert_status)](https://sonarcloud.io/dashboard?id=Bonitasoft-Community_users-list-actorfilter)
[![GitHub version](https://badge.fury.io/gh/Bonitasoft-Community%2Fusers-list-actorfilter.svg)](https://badge.fury.io/gh/Bonitasoft-Community%2Fusers-list-actorfilter)

Select a sublist of candidates for a task among the list of users of the Actor

## Build

Run `./mvnw`

## Usage

1. Import `target/users-list-actorfilter-<version>.zip` in your project using Bonita Studio (Development > Actor Filter > Import...)
1. Open your process diagram, on a task of your process go to the `General > Actors` tab of the property view
1. Click on `Set...` next to `Actor filter`
1. Select `Organization` category, `Users list` filter and click `Next`
1. Define a name and description for this filter and click `Next`
1. Use existing variable containing the desired users list (_List<java.lang.Long>_) or use a groovy script for advanced usage
1. You can choose to auto-assign the task if the returned list contains a single user
