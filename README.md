# Piano-Hometask

This project introduces the utility of parsing natural language phrases for conversion info request to the object-based form. To test this utility you may build it manually with Maven and using the generated executable launcer-<VERSION>-jar-with-dependencies.jar or use the prepared executable file in /exe folder by running the piano-hometask.bat file.
After launching you should see the empty console screen expecting your input. Type 'help' (without qoutes) to see documentation over the console utility and to get next instructions for input. 

The output model is following:<br/>
1. "From-State" entry is information about state from which the user moved.
2. "To-State" entry is information about state to which the user moved.
3. Locations - list of locations for which the client wants to know conversion info.
4. Tools - list of tools which used by users for conversions. Each tool is set of characteristics about: DEVICE, OS, BROWSER, used APPLICATION, REFERRAL_CHANNEL.
5. Time range - interval of time to give the output data within.
6. Participants - list of subjects that participated in conversion event (e. g. CLIENT or REFERRER).
7. State domains - list of state domains. Each state domain is info about within what the user changed his state. For now it represents information about metric group, metric and term.
8. Operators descriptor - list of operators which should be applied to result set. Includes: grouping, aggregating, selecting, pagination. Each operator is descriptor of operation that should be applied, not real operator.