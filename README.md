# Coding-Challenge
You are tasked with assisting satellite ground operations for an earth science mission that monitors magnetic field variations at the Earth's poles. A pair of satellites fly in tandem orbit such that at least one will have line of sight with a pole to take accurate readings. The satellite’s science instruments are sensitive to changes in temperature and must be monitored closely. Onboard thermostats take several temperature readings every minute to ensure that the precision magnetometers do not overheat. Battery systems voltage levels are also monitored to ensure that power is available to cooling coils. Design a monitoring and alert application that processes status telemetry from the satellites and generates alert messages in cases of certain limit violation scenarios.


Requirements
Ingest status telemetry data and create alert messages for the following violation conditions:

If for the same satellite there are three battery voltage readings that are under the red low limit within a five minute interval.
If for the same satellite there are three thermostat readings that exceed the red high limit within a five minute interval.
