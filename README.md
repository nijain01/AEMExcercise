# Sample Assignment Description

It contains three functionalities:
1.  Add custom property whenever a page is created
2.  New Breadcrumb component to show in order en(link) > us(linked) > Page Name (Page is in en/us/paage)
3.  When user edits the component, there should be a tag picker. When user selects a tag, it should display all the pages tagged  

## Approach

For first functionality (custom property), two approaches are used:
1.  Workflow Process step: Using it a Workflow is created which has only this single step and then Launcher is created
2.  Event Handler: Listens for "Page Created" event and adds a custom property

For second functionality (breadcrumb), a new component is created (apps/appleassignment/components/breadcrumb)

For third Functionality (tag picker), a new component is created (apps/appleassignment/components/tagpicker). 
Approach: Author can select multiple tags from the component. In the model class, Query builder is used to select 
the pages that are tagged with the selected tag(s).   

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

Or alternatively

    mvn clean install -PautoInstallSinglePackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

Or to deploy only a single content package, run in the sub-module directory (i.e `ui.apps`)

    mvn clean install -PautoInstallPackage

## Testing

There are three levels of testing contained in the project:

### Unit tests

This show-cases classic unit testing of the code contained in the bundle. To
test, execute:

    mvn clean test