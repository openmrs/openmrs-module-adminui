# Contributing to the module

## Resources for Getting Started
- Before you get started contributing to OpenMRS, we encourage you to read both our [OpenMRS Developers Guide](http://go.openmrs.org/newdev-web) and our [Getting Started as a Developer](https://wiki.openmrs.org/x/MQAJ) wiki page.
- Remember to take a look at the [using git] (https://wiki.openmrs.org/x/KQQdAg) documentation.

## Contributing
- Make sure you have cloned the OpenMRS core source code, see the [getting started guide](https://wiki.openmrs.org/x/poBE)
- Make sure you are on the development branch that matches the openmrs version, e.g if the version is 1.11.5-SNAPSHOT, then the OpenMRS working branch is 1.11.x branch
- Fork the [source code repository](https://github.com/openmrs/openmrs-module-adminui)
- Clone the repository from your fork, make sure you can compile and build the project successfully.
- Claim a ticket to work on from [here](https://issues.openmrs.org/browse/TRUNK-4748?jql=labels%20%3D%20adminui), select a ticket whose status is 'Ready for Work'.
- Create a branch with the name matching the ticket number e.g RA-150
- After committing your changes to your branch, push the code to your fork.
- Create a pull request in github, refer to our [pull requests tips](https://wiki.openmrs.org/x/KYOfAw)
- Another developer will review your code and possibly add some comments that you might need to address otherwise if all is well they will go ahead to merge and close your pull request, note that if you are required to make more changes, you DO NOT have to create a new pull request, you can always commit and push to the same branch and github will automatically update your original pull request as long it isn't yet merged and not yet closed.
- Once your code has been merged and the pull request has been closed, you can delete your working branch from your local repository and fork.
