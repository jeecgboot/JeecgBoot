package org.jeecg.modules.im.botz;

import org.igniterealtime.openfire.botz.BotzConnection;
import org.igniterealtime.openfire.botz.BotzPacketReceiver;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import java.io.File;

public class ParrotBot implements Plugin
{
    @Override
    public void destroyPlugin() {}
    
    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory)
    {
        BotzPacketReceiver packetReceiver = new BotzPacketReceiver() {
            BotzConnection bot;
            
            public void initialize(BotzConnection bot) {
                this.bot = bot;
            }
            
            public void processIncoming(Packet packet) {
                if (packet instanceof Message) {
                    // Echo back to sender
                    packet.setTo(packet.getFrom());
                    bot.sendPacket(packet);
                }
            }
            public void processIncomingRaw(String rawText) { };
            
            public void terminate() { };
        };  
        
        BotzConnection bot = new BotzConnection(packetReceiver);
        try {
            // Create user and login
            bot.login("parrot");
            Presence presence = new Presence();
            presence.setStatus("Online");
            bot.sendPacket(presence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}