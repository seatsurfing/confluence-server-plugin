# Confluence Server & Datacenter Plugin for Seatsurfing

This is the Confluence Server and Datacenter Plugin for Seatsurfing built with the Atlassian Plugin SDK.

The plugin basically renders an iFrame and loads the Seatsurfing Booking UI from the configured URL. In order to ensure that the backend knows which user is using Atlassian Confluence, this Confluence App encodes the information about the current user using a symmetrically encoded JWT passed to the configured backend.

## What's next?
[Seatsurfing for Confluence in the Atlassian Marketplace](https://marketplace.atlassian.com/apps/1224242/seatsurfing-for-confluence)

## Essential Atlassian Plugin SDK commands
* atlas-run   -- installs this plugin into the product and starts it on localhost
* atlas-debug -- same as atlas-run, but allows a debugger to attach at port 5005
* atlas-help  -- prints description for all commands in the SDK

Full documentation is always available at:

https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK
