package com.gameserver;

import com.gameserver.data.IItemData;
import com.gameserver.data.INpcData;
import com.gameserver.data.ISkillData;
import com.gameserver.data.ISpawnData;
import com.gameserver.data.ItemData;
import com.gameserver.data.NpcData;
import com.gameserver.data.SkillData;
import com.gameserver.data.SpawnData;
import com.gameserver.managers.DatabaseManager;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.network.packets.receivable.*;
import com.gameserver.network.packets.receivable.IRequestHandler;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import static com.gameserver.enums.ClientRequestType.*;

public class ServerModule extends AbstractModule {
	@Override
    public void configure() {
		bind(IDatabaseManager.class).to(DatabaseManager.class).asEagerSingleton();
		bind(INpcData.class).to(NpcData.class).asEagerSingleton();
		bind(IItemData.class).to(ItemData.class).asEagerSingleton();
		bind(ISpawnData.class).to(SpawnData.class).asEagerSingleton();
		bind(ISkillData.class).to(SkillData.class).asEagerSingleton();
		
		MapBinder<String, IRequestHandler> clientRequestHandlersBinder =
		        MapBinder.newMapBinder(binder(), String.class, IRequestHandler.class);
		clientRequestHandlersBinder.addBinding(ACCOUNT_AUTHENTICATION.name()).to(AccountAuthenticationRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(CHARACTER_SELECTION_INFO.name()).to(CharacterSelectionInfoRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(CHARACTER_CREATION.name()).to(CharacterCreationRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(CHARACTER_DELETION.name()).to(CharacterDeletionRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(CHARACTER_SLOT_UPDATE.name()).to(CharacterSlotUpdate.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(CHARACTER_SELECT_UPDATE.name()).to(CharacterSelectUpdate.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(ENTER_WORLD.name()).to(EnterWorldRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(EXIT_WORLD.name()).to(ExitWorldRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(LOCATION_UPDATE.name()).to(LocationUpdateRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(ANIMATOR_UPDATE.name()).to(AnimatorUpdateRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(OBJECT_INFO.name()).to(ObjectInfoRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(PLAYER_OPTIONS_UPDATE.name()).to(PlayerOptionsUpdate.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(CHAT_REQUEST.name()).to(ChatRequest.class).asEagerSingleton();
		clientRequestHandlersBinder.addBinding(TARGET_UPDATE.name()).to(TargetUpdateRequest.class).asEagerSingleton();

	}

}
