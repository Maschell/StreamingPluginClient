# Streaming Plugin Client

A Java client for the [Wii U Streaming Plugin](https://github.com/Maschell/StreamingPluginWiiU).

# Usage

Start the plugin via the [WiiUPluginSystemLoader](https://github.com/Maschell/WiiUPluginSystem) on your WiiU. Now start Java client by either
- provide the ip address of your Wii U as a argument. Example: ``java -jar StreamingPluginClient[...].jar --ip 192.168.0.11`
- **or** just double click on the .jar. A little dialog should appear where you can enter the IP address of your Wii U console.

Requires Java 8.

# Building

This is a maven project. Use following command to create a .jar with dependencies in the `target` folder. 
```
mvn package
```