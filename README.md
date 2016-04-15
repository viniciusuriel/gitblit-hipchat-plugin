## Gitblit HipChat plugin

This is a modified version of the original plugin provided by Jarmes Morger.
What is changed:
  - I have introduced a more flexible configuration model allowing arbitrary naming for rooms and mapping between rooms and repositories.
  - Now we send the full commit message instead of the short one.


*REQUIRES 1.5.0+*

The Gitblit HipChat plugin provides realtime integration for your HipChat team.  The plugin inject events into a room for branch or tag changes and ticket changes.

![example](example.png "Example integration")

### Installation

Build (see section Building against a Gitblit RELEASE)

Alternatively, you can download the zip from [here](http://plugins.gitblit.com) manually copy it to your `${baseFolder}/plugins` directory.

### Setup

There are a few changes compared with the original version published by James Morger.

You will need three  properties  configured in `gitblit.properties` for each room.
Each room configuration should be identified by an integer counter (zero based).
The example below show how to configure a single room:

    hipchat.room.0 = <room_name>
    hipchat.room.0.token = <token>
    hipchat.room.0.repo = <repo_name>
    
For the sake of simplicity this plugin will scan for 200 rooms configurations only.
I have chosen to keep the concept of defaultRoom, in this case the lowest index room is the defaultRoom

There a handful of additional optional settings:

    hipchat.postPersonalRepos = false
    hipchat.postTickets = true
    hipchat.postTicketComments = true
    hipchat.postBranches = true
    hipchat.postTags = true

### Usage

#### Ticket Hook

The ticket hook is automatic.

#### Receive Hook

The receive hook is automatic.


### Building against a Gitblit RELEASE

    ant && cp build/target/hipchat*.zip /path/to/gitblit/plugins

### Building against a Gitblit SNAPSHOT

    /path/to/dev/gitblit/ant installMoxie
    /path/to/dev/hipchat/ant && cp build/target/hipchat*.zip /path/to/gitblit/plugins

