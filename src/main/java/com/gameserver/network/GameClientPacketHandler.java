//package com.gameserver.network;
//
//import java.util.Map;
//
//import com.gameserver.network.packets.receivable.AccountAuthenticationRequest;
//import com.gameserver.network.packets.receivable.AnimatorUpdateRequest;
//import com.gameserver.network.packets.receivable.CharacterCreationRequest;
//import com.gameserver.network.packets.receivable.CharacterDeletionRequest;
//import com.gameserver.network.packets.receivable.CharacterSelectUpdate;
//import com.gameserver.network.packets.receivable.CharacterSelectionInfoRequest;
//import com.gameserver.network.packets.receivable.CharacterSlotUpdate;
//import com.gameserver.network.packets.receivable.ChatRequest;
//import com.gameserver.network.packets.receivable.EnterWorldRequest;
//import com.gameserver.network.packets.receivable.ExitWorldRequest;
//import com.gameserver.network.packets.receivable.IRequestHandler;
//import com.gameserver.network.packets.receivable.LocationUpdateRequest;
//import com.gameserver.network.packets.receivable.ObjectInfoRequest;
//import com.gameserver.network.packets.receivable.PlayerOptionsUpdate;
//import com.gameserver.network.packets.receivable.TargetUpdateRequest;
//import com.google.inject.Inject;
//
//public class GameClientPacketHandler implements IGameClientPacketHandler
//{
//	
//	
//	
//	
//	
//	public GameClientPacketHandler() {
//		
//	}
//
//
//
//	@Override
//	public void handle(GameClient client, ReceivablePacket packet)
//	{
//		switch (packet.readShort()) // Packet id.
//		{
//			case 1:
//				new AccountAuthenticationRequest(client, packet);
//				break;
//			
//			case 2:
//				new CharacterSelectionInfoRequest(client, packet);
//				break;
//			
//			case 3:
//				new CharacterCreationRequest(client, packet);
//				break;
//			
//			case 4:
//				new CharacterDeletionRequest(client, packet);
//				break;
//			
//			case 5:
//				new CharacterSlotUpdate(client, packet);
//				break;
//			
//			case 6:
//				new CharacterSelectUpdate(client, packet);
//				break;
//			
//			case 7:
//				new EnterWorldRequest(client, packet);
//				break;
//			
//			case 8:
//				new ExitWorldRequest(client, packet);
//				break;
//			
//			case 9:
//				new LocationUpdateRequest(client, packet);
//				break;
//			
//			case 10:
//				new AnimatorUpdateRequest(client, packet);
//				break;
//			
//			case 11:
//				new ObjectInfoRequest(client, packet);
//				break;
//			
//			case 12:
//				new PlayerOptionsUpdate(client, packet);
//				break;
//			
//			case 13:
//				new ChatRequest(client, packet);
//				break;
//			
//			case 14:
//				new TargetUpdateRequest(client, packet);
//				break;
//			default:
//				System.err.println("Undefined Request !! " + packet.readString());
//				break;
//		}
//	}
//}
