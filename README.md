# Users List Actor Filter

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
