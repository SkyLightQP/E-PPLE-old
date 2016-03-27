package me.efe.efeserver.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

public class UserUtils {
	private static Set<UUID> cbtTesters = new HashSet<UUID>();
	private static Set<UUID> obtTesters = new HashSet<UUID>();
	private static Map<UUID, Integer> oldUsers = new HashMap<UUID, Integer>();
	
	static {
		cbtTesters.add(UUID.fromString("0c33be12-3856-40aa-b97e-e8599bc92709"));
		cbtTesters.add(UUID.fromString("0d26e11d-a56a-4c60-8758-2afded8d126e"));
		cbtTesters.add(UUID.fromString("0f466e1d-5946-49bf-b8cb-2140b0c638cc"));
		cbtTesters.add(UUID.fromString("102dd87b-1332-4d85-9a5e-b5c3179d1a64"));
		cbtTesters.add(UUID.fromString("1323ce8f-b0ee-4108-83be-b60535dd35e3"));
		cbtTesters.add(UUID.fromString("15beb78e-346f-472b-a3b8-66823f47286e"));
		cbtTesters.add(UUID.fromString("16232369-4b88-402f-adad-137c2d43920c"));
		cbtTesters.add(UUID.fromString("1aa0b567-7944-45e3-bb90-f105dac2d63e"));
		cbtTesters.add(UUID.fromString("20bf454f-34e3-4010-a378-613546e3d0f9"));
		cbtTesters.add(UUID.fromString("2f543fcc-d3f0-4afd-aa9d-4fca4a683859"));
		cbtTesters.add(UUID.fromString("3813b706-e555-44fd-9b83-227b3f1e603c"));
		cbtTesters.add(UUID.fromString("3e44b25d-5808-4aa7-8f99-d635e612bea3"));
		cbtTesters.add(UUID.fromString("42bad32e-eba7-41fd-a129-25f701684217"));
		cbtTesters.add(UUID.fromString("46d2d162-1a7d-4737-bf8e-7dca9915a59d"));
		cbtTesters.add(UUID.fromString("4f03ee8f-4b6a-45c0-b488-b5da1dbfdcf1"));
		cbtTesters.add(UUID.fromString("7f85764b-6b52-49ec-9f8a-48b626192902"));
		cbtTesters.add(UUID.fromString("8139bbda-91c9-4d74-97d3-f64aa2631ec2"));
		cbtTesters.add(UUID.fromString("855c4130-5b73-4759-8a8a-3a2f5bc1fdd4"));
		cbtTesters.add(UUID.fromString("a0258f54-1d21-4349-9b31-0fd4efcc0deb"));
		cbtTesters.add(UUID.fromString("a267657f-883b-441b-9727-0a6c91caffff"));
		cbtTesters.add(UUID.fromString("b15b2196-2f70-4cc0-b2c5-c54e715f0afb"));
		cbtTesters.add(UUID.fromString("bae22ab8-90e0-43ff-8ad5-6431d6993a6b"));
		cbtTesters.add(UUID.fromString("bc30caa7-6ef3-409a-a28a-f37a1abd67f0"));
		cbtTesters.add(UUID.fromString("bc44a433-3d52-4f3b-9ec1-686764cf1d43"));
		cbtTesters.add(UUID.fromString("c5fc51e9-81eb-4f77-929d-bf341c56983d"));
		cbtTesters.add(UUID.fromString("c86313c2-ad1e-4754-84db-3abbd223a6a7"));
		cbtTesters.add(UUID.fromString("d0d28648-4ea9-423f-bf1b-ad67d7f52378"));
		cbtTesters.add(UUID.fromString("d1696131-8691-4563-956c-fa179c70891c"));
		cbtTesters.add(UUID.fromString("d5f53821-a2fa-4dd3-9243-1360bb252eea"));
		cbtTesters.add(UUID.fromString("dba13bf1-5fce-479c-9f04-bb130e4a69ab"));
		cbtTesters.add(UUID.fromString("e17a5880-a517-422b-82bf-c4c0dc275415"));
		cbtTesters.add(UUID.fromString("ea63228f-01a1-495b-9099-66e6d1e162fa"));
		cbtTesters.add(UUID.fromString("ed79257b-0ee9-4572-8235-e1f788576711"));
		cbtTesters.add(UUID.fromString("edd5e9aa-1ac4-4aa7-8039-20645f7c9b1e"));
		cbtTesters.add(UUID.fromString("f7b3600b-6ce7-4606-a54a-80e2e296c528"));
		cbtTesters.add(UUID.fromString("fa90ba90-9446-4141-83c5-6b31487112c3"));
		cbtTesters.add(UUID.fromString("fccbcbfa-c869-4712-8736-802dfe225b46"));
		cbtTesters.add(UUID.fromString("fe557cd8-050b-4a89-93f9-5c0b70ba584d"));
		
		obtTesters.add(UUID.fromString("0170e57c-9dc9-4235-bb14-0cfc85c846ab"));
		obtTesters.add(UUID.fromString("0211e057-3749-4fce-8099-e7e6bcfe5860"));
		obtTesters.add(UUID.fromString("044d5d67-8d62-4e95-af5f-1efad380a3ad"));
		obtTesters.add(UUID.fromString("04832c2c-70d2-4b57-99fb-75f41829fe2a"));
		obtTesters.add(UUID.fromString("0b74522a-8e56-4c7a-b97d-8b201e77d588"));
		obtTesters.add(UUID.fromString("0cdcbfee-d197-452e-a1d9-cf0ab16e6544"));
		obtTesters.add(UUID.fromString("0eb915de-9026-4a65-9b9b-93213db0daf4"));
		obtTesters.add(UUID.fromString("102dd87b-1332-4d85-9a5e-b5c3179d1a64"));
		obtTesters.add(UUID.fromString("12a546e1-1996-42fc-b1e8-1d4cbb679cb9"));
		obtTesters.add(UUID.fromString("1323ce8f-b0ee-4108-83be-b60535dd35e3"));
		obtTesters.add(UUID.fromString("16221029-bd9c-44bf-af47-46b7d2e65e7c"));
		obtTesters.add(UUID.fromString("16232369-4b88-402f-adad-137c2d43920c"));
		obtTesters.add(UUID.fromString("19859a42-67af-4d1d-85f7-1ee7c31461e9"));
		obtTesters.add(UUID.fromString("1ac8e193-a580-4bb9-9a2f-a0c8b8f18e03"));
		obtTesters.add(UUID.fromString("1babc4b5-c4c6-4cc4-9937-e4c4d0176c34"));
		obtTesters.add(UUID.fromString("203a1ba6-7ff8-4332-8c6e-a318f24eec9c"));
		obtTesters.add(UUID.fromString("20bf454f-34e3-4010-a378-613546e3d0f9"));
		obtTesters.add(UUID.fromString("3203b0e0-9d10-4ca6-8657-664ebdc5f805"));
		obtTesters.add(UUID.fromString("36326db4-5963-48b5-b109-be25a80571a3"));
		obtTesters.add(UUID.fromString("37041a1b-3e14-494e-8d96-3e36c471d86a"));
		obtTesters.add(UUID.fromString("3813b706-e555-44fd-9b83-227b3f1e603c"));
		obtTesters.add(UUID.fromString("39eed3a1-0681-43c2-ba38-488b8334cc90"));
		obtTesters.add(UUID.fromString("3c95f8a1-45a0-42b8-b26a-e8dd2f78dc6e"));
		obtTesters.add(UUID.fromString("41104e51-bf34-41c4-91a9-b7d3012d8913"));
		obtTesters.add(UUID.fromString("437ef6a6-02de-4c3d-872c-bd5238e41edb"));
		obtTesters.add(UUID.fromString("43c825e7-472f-4b92-80d0-4b8409fe2be2"));
		obtTesters.add(UUID.fromString("468e344c-f319-461c-8ca8-2e7d55bcf2d1"));
		obtTesters.add(UUID.fromString("495088b4-2547-4cdd-862e-c558d2363635"));
		obtTesters.add(UUID.fromString("4bab89d6-69fd-4cbd-acfd-740113afe2e0"));
		obtTesters.add(UUID.fromString("4f2bb6dc-9d8f-418c-9e8f-082c9bf56e20"));
		obtTesters.add(UUID.fromString("50cda927-d6dd-4a10-a406-cef138b7c642"));
		obtTesters.add(UUID.fromString("52ea0b62-5636-4567-b02e-a9a1009e124a"));
		obtTesters.add(UUID.fromString("550567d9-dfa9-45d8-9d84-695b2534069e"));
		obtTesters.add(UUID.fromString("5508b77f-0eac-4733-86e4-6c49b309879e"));
		obtTesters.add(UUID.fromString("559b8cff-3fc1-474c-9057-14175a5f3383"));
		obtTesters.add(UUID.fromString("58607cf1-b225-4d05-90cb-6f9674beba58"));
		obtTesters.add(UUID.fromString("61453257-bde2-4e30-b71a-990ceb45b6e6"));
		obtTesters.add(UUID.fromString("64bf0e44-2a85-4ec8-ad08-3b511de0c224"));
		obtTesters.add(UUID.fromString("661d6611-cef9-4002-a067-5901a278d84e"));
		obtTesters.add(UUID.fromString("683a1bf7-0204-4e73-a634-d158ae8723b9"));
		obtTesters.add(UUID.fromString("6b92b3f5-1b7d-4b14-b81c-e11f9fc79b17"));
		obtTesters.add(UUID.fromString("79e5256d-c14b-4994-8d20-32e695215ec8"));
		obtTesters.add(UUID.fromString("7ea08f83-cb12-4a63-97a4-7d539913f688"));
		obtTesters.add(UUID.fromString("7f5bdb97-78e7-40a9-bf00-30fefbc730cc"));
		obtTesters.add(UUID.fromString("7f85764b-6b52-49ec-9f8a-48b626192902"));
		obtTesters.add(UUID.fromString("8139bbda-91c9-4d74-97d3-f64aa2631ec2"));
		obtTesters.add(UUID.fromString("855c4130-5b73-4759-8a8a-3a2f5bc1fdd4"));
		obtTesters.add(UUID.fromString("881e37d7-8a7d-4e2b-a372-6d8a3794e923"));
		obtTesters.add(UUID.fromString("88f9a1ca-ca08-4d1d-8860-1504409e4637"));
		obtTesters.add(UUID.fromString("8b363fb8-1f4c-4850-a54a-917ae0c955f8"));
		obtTesters.add(UUID.fromString("8cee4870-15f3-475a-9048-5975db575b65"));
		obtTesters.add(UUID.fromString("8e1ab360-5ec9-41b1-99da-7516c686750d"));
		obtTesters.add(UUID.fromString("911bc1e5-fc9c-407c-8aa6-b1f5f888f6bf"));
		obtTesters.add(UUID.fromString("9334acd5-21e0-4718-a0e6-a536961217ab"));
		obtTesters.add(UUID.fromString("942a6e3a-4044-4556-a095-35a0a73ddff2"));
		obtTesters.add(UUID.fromString("97e4ffd4-376b-4dd6-a8b8-703c00fad76d"));
		obtTesters.add(UUID.fromString("9ed5662e-8af2-43ad-b5a2-026b36b87357"));
		obtTesters.add(UUID.fromString("a0258f54-1d21-4349-9b31-0fd4efcc0deb"));
		obtTesters.add(UUID.fromString("a267657f-883b-441b-9727-0a6c91caffff"));
		obtTesters.add(UUID.fromString("a53a0b6e-146a-4786-b6b5-0c73ff0fa4d4"));
		obtTesters.add(UUID.fromString("a732c5ff-8348-48a4-9e5a-746e39a61be9"));
		obtTesters.add(UUID.fromString("aaedd5dd-eb19-4a2a-9f05-bbcd2510520c"));
		obtTesters.add(UUID.fromString("aef4e44f-8a8f-4987-989a-57b4515745f5"));
		obtTesters.add(UUID.fromString("af70b92a-4ce3-454d-9862-57b67d074460"));
		obtTesters.add(UUID.fromString("b15b2196-2f70-4cc0-b2c5-c54e715f0afb"));
		obtTesters.add(UUID.fromString("b2ebf0e5-bfe2-4a38-8caf-c8446c38ca2a"));
		obtTesters.add(UUID.fromString("b4665e99-287c-45e2-b031-6d7489b09c0b"));
		obtTesters.add(UUID.fromString("b4c0de2c-0098-45bd-b60d-faa017b983b3"));
		obtTesters.add(UUID.fromString("b544f9bc-f4ed-4def-a571-6c7e72a945d0"));
		obtTesters.add(UUID.fromString("b69fd29a-93e0-460b-9c4e-0bac882e6ecd"));
		obtTesters.add(UUID.fromString("b824e65e-8509-46ec-a31d-4ab1821ac232"));
		obtTesters.add(UUID.fromString("b8624fbd-8285-4610-8bed-eb2b0a2e5bdc"));
		obtTesters.add(UUID.fromString("b966aa14-2290-4850-9b35-09a7890ae6a8"));
		obtTesters.add(UUID.fromString("b98ad8ae-27bc-4c08-95fd-64fe48cfc7d1"));
		obtTesters.add(UUID.fromString("b9961ec4-08eb-4033-8682-b2b131f500ec"));
		obtTesters.add(UUID.fromString("b9f6efd4-e713-4f0f-8abf-028491018136"));
		obtTesters.add(UUID.fromString("bae22ab8-90e0-43ff-8ad5-6431d6993a6b"));
		obtTesters.add(UUID.fromString("bc30caa7-6ef3-409a-a28a-f37a1abd67f0"));
		obtTesters.add(UUID.fromString("bc44a433-3d52-4f3b-9ec1-686764cf1d43"));
		obtTesters.add(UUID.fromString("bd1e740a-e353-4031-884e-e6b17fa62300"));
		obtTesters.add(UUID.fromString("bee563f9-a9a7-4e2b-90b5-881119cc608f"));
		obtTesters.add(UUID.fromString("c39262f4-3dd5-44ec-878e-2d6d7d93fd0b"));
		obtTesters.add(UUID.fromString("c54de051-188d-4d57-833c-83c14bb19075"));
		obtTesters.add(UUID.fromString("c5fc51e9-81eb-4f77-929d-bf341c56983d"));
		obtTesters.add(UUID.fromString("cb279d1b-cba7-478f-b7cc-78fe100e266c"));
		obtTesters.add(UUID.fromString("d254b61b-0ecf-4c5f-b935-bbe9b6e07dd1"));
		obtTesters.add(UUID.fromString("d2c2a976-0e6c-42ee-9be1-093106807a58"));
		obtTesters.add(UUID.fromString("d5146713-4053-43f2-9aa1-a4e6a2f2530a"));
		obtTesters.add(UUID.fromString("d7ccae50-ccb2-4ef7-9c14-3d82a37db583"));
		obtTesters.add(UUID.fromString("d8c7d867-d482-43b7-bc61-8110815a92cf"));
		obtTesters.add(UUID.fromString("dba13bf1-5fce-479c-9f04-bb130e4a69ab"));
		obtTesters.add(UUID.fromString("dce5c853-dc2c-418e-9698-77a9719c8478"));
		obtTesters.add(UUID.fromString("df1b73c0-4905-40cb-b50d-fcde9b616866"));
		obtTesters.add(UUID.fromString("e01f379f-2482-4fe3-ae6b-26dd4d0c9f36"));
		obtTesters.add(UUID.fromString("e17a5880-a517-422b-82bf-c4c0dc275415"));
		obtTesters.add(UUID.fromString("e292f279-ee8d-485b-918c-09b218b6b606"));
		obtTesters.add(UUID.fromString("e5a57d93-f37f-45bd-a3fc-36fd19199b00"));
		obtTesters.add(UUID.fromString("ea46ecbe-5f9d-4984-ab8b-b493e3d6e1a6"));
		obtTesters.add(UUID.fromString("eb75fe95-9f21-4e35-9c28-86f549f256cf"));
		obtTesters.add(UUID.fromString("f4552182-ed79-4b56-a738-445596e35d7b"));
		obtTesters.add(UUID.fromString("f744a66a-107e-49e3-adff-746aaa6c0f0b"));
		obtTesters.add(UUID.fromString("f7b3600b-6ce7-4606-a54a-80e2e296c528"));
		obtTesters.add(UUID.fromString("fc5eac54-9f64-4759-b5a5-bc216e4e6856"));
		obtTesters.add(UUID.fromString("fe557cd8-050b-4a89-93f9-5c0b70ba584d"));
		
		oldUsers.put(UUID.fromString("0354fb0d-1739-411e-9b89-cc67e898eb5f"), 12);
		oldUsers.put(UUID.fromString("03a5f28d-4376-443f-837e-5092afc1ff33"), 0);
		oldUsers.put(UUID.fromString("04a4be5a-a2ae-4021-b0b9-446be5927690"), 0);
		oldUsers.put(UUID.fromString("05520941-363c-4b2a-8d4e-4f7469d932e0"), 1);
		oldUsers.put(UUID.fromString("05b1c43e-8d36-40fa-a209-c1dbe5445fb4"), 7);
		oldUsers.put(UUID.fromString("05cecbc9-dec8-47ce-a238-43d4de474ac0"), 1);
		oldUsers.put(UUID.fromString("07e15398-f257-4819-8850-e5ccf16a3b87"), 1);
		oldUsers.put(UUID.fromString("084514a5-0b03-41cf-8725-dc01d69e9d59"), 1);
		oldUsers.put(UUID.fromString("08b4dc52-ec94-415f-a1d2-16e461306308"), 1);
		oldUsers.put(UUID.fromString("0a9a531e-2a64-4f37-af81-fc9370cc4b5f"), 2);
		oldUsers.put(UUID.fromString("0aedc2af-a999-48d5-a911-dbbd31b7ed49"), 0);
		oldUsers.put(UUID.fromString("0d26e11d-a56a-4c60-8758-2afded8d126e"), 2);
		oldUsers.put(UUID.fromString("102dd87b-1332-4d85-9a5e-b5c3179d1a64"), 1);
		oldUsers.put(UUID.fromString("117bc9b7-7f7f-4be8-a7f8-c21c8ce396ef"), 1);
		oldUsers.put(UUID.fromString("1297f445-d1d4-4e16-aa92-553f67f6438c"), 0);
		oldUsers.put(UUID.fromString("131b44eb-7b0c-42b0-884d-bc7eadde1ed8"), 0);
		oldUsers.put(UUID.fromString("16232369-4b88-402f-adad-137c2d43920c"), 9);
		oldUsers.put(UUID.fromString("1698f9ce-db2f-4ce9-a9d7-61fbac3374d6"), 0);
		oldUsers.put(UUID.fromString("16cb668a-1e35-453b-89a8-74c939804d65"), 1);
		oldUsers.put(UUID.fromString("16d58770-253e-46e1-8d46-421c1fc43605"), 10);
		oldUsers.put(UUID.fromString("181ce6e0-6ad9-4c74-949f-4cc04a5370f2"), 6);
		oldUsers.put(UUID.fromString("184fb1a6-6383-428c-9cc9-810387bf57b7"), 1);
		oldUsers.put(UUID.fromString("192831af-ce87-4d5f-b6dc-ad422fdf2265"), 6);
		oldUsers.put(UUID.fromString("1970ebae-9106-4f58-a870-ba65bf16ad1a"), 19);
		oldUsers.put(UUID.fromString("1c15530d-091b-43d7-81e0-4251f2cd1684"), 1);
		oldUsers.put(UUID.fromString("1cc1f88d-0998-4411-8280-9cf236a678d4"), 1);
		oldUsers.put(UUID.fromString("1d3701cc-4667-4555-8206-cb1d9be54c69"), 5);
		oldUsers.put(UUID.fromString("1d6fab02-a952-4ffa-b7a0-73a93a6a08a8"), 18);
		oldUsers.put(UUID.fromString("1dbc7dae-a7e9-40e7-875f-89c75b93e59f"), 0);
		oldUsers.put(UUID.fromString("1f57f6c1-83d8-41e6-aadd-559ac11fb00a"), 6);
		oldUsers.put(UUID.fromString("1faa12dc-ba54-4427-837d-21c5f941ecba"), 0);
		oldUsers.put(UUID.fromString("203a1ba6-7ff8-4332-8c6e-a318f24eec9c"), 6);
		oldUsers.put(UUID.fromString("20bf454f-34e3-4010-a378-613546e3d0f9"), 1);
		oldUsers.put(UUID.fromString("21004fce-1429-456d-847a-b50e18e41eee"), 10);
		oldUsers.put(UUID.fromString("2382726c-ccd5-47a1-a27d-6631f26f441a"), 1);
		oldUsers.put(UUID.fromString("23d7b129-5ca8-43cb-9613-fbe4f98e8fd3"), 0);
		oldUsers.put(UUID.fromString("246c9935-3d6b-4841-82b2-149706ddba98"), 1);
		oldUsers.put(UUID.fromString("251674e7-beb2-48ba-a075-84e97928e5b1"), 0);
		oldUsers.put(UUID.fromString("25863475-fc8c-4a4a-a3c8-cf2707b3b4f5"), 1);
		oldUsers.put(UUID.fromString("25ba8f48-eac1-44c6-a220-395ef652abe0"), 1);
		oldUsers.put(UUID.fromString("25ff01db-3e50-4a6b-8cc9-b48650fe55cb"), 1);
		oldUsers.put(UUID.fromString("26156a93-9b9b-412d-b1a3-85cef9a7ca41"), 0);
		oldUsers.put(UUID.fromString("27276ba3-e8bd-487e-9fee-1edf26e027c9"), 4);
		oldUsers.put(UUID.fromString("278f037f-2a22-4525-9b87-d25844f19afe"), 1);
		oldUsers.put(UUID.fromString("27be4059-dc85-4f09-9c28-52e5fa46a9e2"), 1);
		oldUsers.put(UUID.fromString("28b0a284-537a-4796-87d5-81d3e90b8c61"), 1);
		oldUsers.put(UUID.fromString("29011e1f-bf45-4990-b39d-9daf97fa5f8a"), 0);
		oldUsers.put(UUID.fromString("2972e356-a5d4-4891-aab5-9d6eb897c32d"), 0);
		oldUsers.put(UUID.fromString("29a58dda-98f4-4eb7-96a4-facd201f64f4"), 1);
		oldUsers.put(UUID.fromString("29a6f769-f0a9-48fd-b905-c32b64420735"), 1);
		oldUsers.put(UUID.fromString("29b1bc4a-467a-4c53-b3b6-bb6adfd591ec"), 4);
		oldUsers.put(UUID.fromString("2a4c336a-d8a4-4303-86fa-b52bc46e3f09"), 3);
		oldUsers.put(UUID.fromString("2e863641-6f7d-4dfc-96e3-ef24f4159929"), 5);
		oldUsers.put(UUID.fromString("301102a8-3c23-4ee7-8f17-0d8f02202612"), 1);
		oldUsers.put(UUID.fromString("3022f77a-985c-487c-9a98-78575a2c8758"), 8);
		oldUsers.put(UUID.fromString("31a4ce07-49ab-4133-bd15-eb46478c31b4"), 3);
		oldUsers.put(UUID.fromString("3203b0e0-9d10-4ca6-8657-664ebdc5f805"), 10);
		oldUsers.put(UUID.fromString("32115768-e836-433c-99ab-bdb1949d12fe"), 1);
		oldUsers.put(UUID.fromString("33a8c4fc-93fc-4e50-aa20-edaf00f36ca4"), 0);
		oldUsers.put(UUID.fromString("33d484c6-e331-4a8d-a39c-c95742d8eb23"), 0);
		oldUsers.put(UUID.fromString("35e5058f-53f5-4a02-a727-82934de391c3"), 1);
		oldUsers.put(UUID.fromString("36b35ca8-b07f-4a91-a744-b0139c28814f"), 0);
		oldUsers.put(UUID.fromString("37041a1b-3e14-494e-8d96-3e36c471d86a"), 0);
		oldUsers.put(UUID.fromString("373da71d-1f55-44b5-9776-79efbe3a06eb"), 0);
		oldUsers.put(UUID.fromString("377557f1-cb64-4422-ad47-273fffb566ac"), 0);
		oldUsers.put(UUID.fromString("3802a8b1-900a-4cbe-9f25-af7ac611061f"), 4);
		oldUsers.put(UUID.fromString("3813b706-e555-44fd-9b83-227b3f1e603c"), 3);
		oldUsers.put(UUID.fromString("38514824-ec69-4710-bd1d-9af9e6c9f476"), 14);
		oldUsers.put(UUID.fromString("390b722f-5f9f-4058-a57e-b37421917f54"), 1);
		oldUsers.put(UUID.fromString("3913a548-4671-49a2-bcd4-aa57d3b0fbbd"), 0);
		oldUsers.put(UUID.fromString("3bd0cf89-c9d9-4f14-8b38-f1e15743689b"), 3);
		oldUsers.put(UUID.fromString("3c446d03-7b8c-4068-97b8-70cdd1e8f322"), 0);
		oldUsers.put(UUID.fromString("3d414fea-84bd-4421-8e9d-f3d9a3d5bdfa"), 3);
		oldUsers.put(UUID.fromString("3e08bc64-6a1c-4ab0-8b82-4ba10d5ce53b"), 1);
		oldUsers.put(UUID.fromString("3fae55f2-ac97-4d3a-9ce5-d6dce2bc7eac"), 1);
		oldUsers.put(UUID.fromString("40a338a7-df59-4704-969a-9d5771ec44f2"), 1);
		oldUsers.put(UUID.fromString("41104e51-bf34-41c4-91a9-b7d3012d8913"), 0);
		oldUsers.put(UUID.fromString("41ea7486-0895-4843-9a7a-612ca2848140"), 10);
		oldUsers.put(UUID.fromString("420f4b9c-beb2-453e-b6f6-f1467384da79"), 0);
		oldUsers.put(UUID.fromString("422331b3-0261-4dbc-a68a-c22a66a36d7d"), 1);
		oldUsers.put(UUID.fromString("437ef6a6-02de-4c3d-872c-bd5238e41edb"), 0);
		oldUsers.put(UUID.fromString("43c825e7-472f-4b92-80d0-4b8409fe2be2"), 2);
		oldUsers.put(UUID.fromString("43e0f3e8-e4bb-415a-b4ea-dbeb8224c42b"), 1);
		oldUsers.put(UUID.fromString("45c1f52c-8be9-4097-aada-a05ba183aebb"), 4);
		oldUsers.put(UUID.fromString("4721ac0d-08a2-416d-88ab-996c696f5a79"), 8);
		oldUsers.put(UUID.fromString("4875f8c9-4f27-4aa9-a256-998b2d7742bf"), 1);
		oldUsers.put(UUID.fromString("48b07b86-0767-4e7b-99ac-2d94a11610c1"), 1);
		oldUsers.put(UUID.fromString("49d238bf-0d26-4f76-9271-e1fe9a34800a"), 11);
		oldUsers.put(UUID.fromString("4b4bc184-d777-4a12-8e22-2a7630586e70"), 1);
		oldUsers.put(UUID.fromString("4c095d77-b08e-4dd9-9ced-5591ae2d4e01"), 15);
		oldUsers.put(UUID.fromString("4dc00323-9aad-4790-8a87-5a82a43418ff"), 0);
		oldUsers.put(UUID.fromString("4e18b090-929d-445b-b9a9-f5568f238b7e"), 1);
		oldUsers.put(UUID.fromString("50cda927-d6dd-4a10-a406-cef138b7c642"), 3);
		oldUsers.put(UUID.fromString("50f9699f-348f-46c1-94fc-bd7236e180a2"), 1);
		oldUsers.put(UUID.fromString("5320829c-5da8-4de6-8aa3-41ed10bb6d78"), 2);
		oldUsers.put(UUID.fromString("532127a2-8d7a-48b1-9719-12ad8032d8a0"), 0);
		oldUsers.put(UUID.fromString("537d059a-9489-4ee7-b9a7-eb2b74929728"), 1);
		oldUsers.put(UUID.fromString("5ae0cfe2-f032-462e-9955-72377995dd11"), 1);
		oldUsers.put(UUID.fromString("5b43a467-6940-42c0-b92f-ed02854c435b"), 2);
		oldUsers.put(UUID.fromString("5b4c6c69-3f4c-48bd-8e3d-5998a48e3051"), 9);
		oldUsers.put(UUID.fromString("5b61a7e2-858b-479f-8da2-b941fe7e3ac4"), 1);
		oldUsers.put(UUID.fromString("5cd0f56e-9fe5-4195-a7e7-52107053b48c"), 10);
		oldUsers.put(UUID.fromString("5d002404-ac29-4135-9763-85264a2c860d"), 0);
		oldUsers.put(UUID.fromString("5f314f2d-a4c7-4f72-b168-05c1870eb631"), 1);
		oldUsers.put(UUID.fromString("5f5746ec-6704-4576-8703-2f8156be190c"), 1);
		oldUsers.put(UUID.fromString("6130555e-7f8c-4cda-8273-3a9da3230c3c"), 2);
		oldUsers.put(UUID.fromString("64bf0e44-2a85-4ec8-ad08-3b511de0c224"), 2);
		oldUsers.put(UUID.fromString("65f0318f-9fd0-448b-a5dd-c340bbe13a9f"), 20);
		oldUsers.put(UUID.fromString("66b6d823-83ba-4276-a68d-7044f5bb6f45"), 6);
		oldUsers.put(UUID.fromString("672f14be-8bdf-46c1-ac63-8db5abdf9604"), 4);
		oldUsers.put(UUID.fromString("683a1bf7-0204-4e73-a634-d158ae8723b9"), 1);
		oldUsers.put(UUID.fromString("69b658f8-5daf-4f02-b1d6-42a3576f4f20"), 10);
		oldUsers.put(UUID.fromString("69c286b3-ec18-4a00-ad20-6fea54ddc3fa"), 0);
		oldUsers.put(UUID.fromString("6a928d2d-e557-4268-9a84-0f96921f0ed8"), 8);
		oldUsers.put(UUID.fromString("6b585e02-9bfb-4ba6-bc31-a5578cade3d0"), 7);
		oldUsers.put(UUID.fromString("6b92b3f5-1b7d-4b14-b81c-e11f9fc79b17"), 1);
		oldUsers.put(UUID.fromString("6bc476fe-f358-4505-b114-d8b65fa8c2fb"), 5);
		oldUsers.put(UUID.fromString("6dded0b0-bfe0-43c1-8da3-5fe862ebf080"), 11);
		oldUsers.put(UUID.fromString("71eda2da-d757-40de-9008-21f573a75db7"), 1);
		oldUsers.put(UUID.fromString("74954f12-eab6-4c68-91ea-55aab9f54357"), 1);
		oldUsers.put(UUID.fromString("75dc9a65-03a7-4e5e-bdda-b3322c553ea9"), 1);
		oldUsers.put(UUID.fromString("7611fa0d-1f11-44b8-be33-3deabdc173d5"), 1);
		oldUsers.put(UUID.fromString("77ee4b09-0c99-4e26-a730-9842c51ec716"), 1);
		oldUsers.put(UUID.fromString("797bfe6f-c10f-4128-8c40-4e65e10cea30"), 0);
		oldUsers.put(UUID.fromString("79e5256d-c14b-4994-8d20-32e695215ec8"), 3);
		oldUsers.put(UUID.fromString("7a87974a-7c3a-4d16-bba5-e537ccbaec75"), 7);
		oldUsers.put(UUID.fromString("7b454012-579c-47eb-a6c6-b774e0c69be4"), 1);
		oldUsers.put(UUID.fromString("7b8117e9-a5bd-4564-b031-a848464623e0"), 1);
		oldUsers.put(UUID.fromString("7bbe7cae-9e47-435f-831e-2cc92c034cba"), 12);
		oldUsers.put(UUID.fromString("7bc7ec45-9046-4ebc-a97a-56e9939e6f98"), 0);
		oldUsers.put(UUID.fromString("7f85764b-6b52-49ec-9f8a-48b626192902"), 5);
		oldUsers.put(UUID.fromString("812f138d-5108-414c-8e19-12a3ee0f370a"), 18);
		oldUsers.put(UUID.fromString("816c24e7-845c-4599-933f-8e6d84dcca78"), 8);
		oldUsers.put(UUID.fromString("81a8d4ee-b155-47b4-b171-f3d845c8fdd8"), 1);
		oldUsers.put(UUID.fromString("820402f1-96ae-430d-bb35-8b12e573eaa4"), 1);
		oldUsers.put(UUID.fromString("821ab83d-5eff-4d80-ae6b-8b9c3736af36"), 0);
		oldUsers.put(UUID.fromString("82632dd2-e7f7-45dc-b783-57e3a173b4a5"), 0);
		oldUsers.put(UUID.fromString("82c5adb9-2334-4043-be9c-10a0424f2599"), 1);
		oldUsers.put(UUID.fromString("8363b86e-0d11-4f88-b5e9-308db78e246f"), 3);
		oldUsers.put(UUID.fromString("85d9afd7-b827-41ab-adc6-b47a69af9e4e"), 11);
		oldUsers.put(UUID.fromString("875d8440-9dfe-4e70-958a-cd593fb8ced3"), 1);
		oldUsers.put(UUID.fromString("87888677-bf30-4308-9157-5920f55b6d77"), 0);
		oldUsers.put(UUID.fromString("87f7d278-9d3b-4a4f-9f23-3c1b2052e25d"), 0);
		oldUsers.put(UUID.fromString("8806ffdc-8236-4c8f-81ed-411951a94bb8"), 0);
		oldUsers.put(UUID.fromString("882c4bfb-f083-4828-ac11-d16ce11d1665"), 0);
		oldUsers.put(UUID.fromString("882e8721-624f-4456-91ce-d457cfe0b3f1"), 8);
		oldUsers.put(UUID.fromString("88f9a1ca-ca08-4d1d-8860-1504409e4637"), 1);
		oldUsers.put(UUID.fromString("8928661b-890d-473c-b18c-9b4103ef19e3"), 1);
		oldUsers.put(UUID.fromString("8952d7fa-20bb-43f3-90a3-7b24de51b307"), 2);
		oldUsers.put(UUID.fromString("8b84bd08-ae02-4493-9f9c-75010a9a8808"), 9);
		oldUsers.put(UUID.fromString("8d26e785-8942-4639-aa85-ea31bc1db6e6"), 2);
		oldUsers.put(UUID.fromString("8df1dab3-abef-4853-acd3-8f27c4965661"), 0);
		oldUsers.put(UUID.fromString("8e68eedf-fab4-4636-b0cb-1a9e9a4bdf5d"), 0);
		oldUsers.put(UUID.fromString("8ed74732-677c-4a9a-8df6-a9566d8fa8e6"), 3);
		oldUsers.put(UUID.fromString("8ed97102-7e2f-4852-b0a2-e95c45d99730"), 0);
		oldUsers.put(UUID.fromString("8fa225ae-ee1a-4874-afa6-255c9d4c0da3"), 0);
		oldUsers.put(UUID.fromString("904ef396-caf7-48cc-8cc3-c468ba0bae92"), 12);
		oldUsers.put(UUID.fromString("90cf727a-168e-48ce-abd6-9d295a630fff"), 1);
		oldUsers.put(UUID.fromString("918e723e-7ddb-4961-bd40-f5912ffdd904"), 3);
		oldUsers.put(UUID.fromString("921fd499-b59c-4610-ab76-57e74a1ed627"), 2);
		oldUsers.put(UUID.fromString("930032ee-ef19-4fa3-8e69-58b22805a460"), 0);
		oldUsers.put(UUID.fromString("952f16af-2b56-4040-9e50-27fa4413cbb7"), 3);
		oldUsers.put(UUID.fromString("957fe5e7-f009-4d8c-8a65-795535b7495a"), 1);
		oldUsers.put(UUID.fromString("95f550e2-8919-4138-bb0f-9d767a5c9083"), 0);
		oldUsers.put(UUID.fromString("9708cc95-ecce-42fd-a986-404eec7372aa"), 1);
		oldUsers.put(UUID.fromString("97f6772e-b1a4-4db7-9c50-a7c2ce618670"), 3);
		oldUsers.put(UUID.fromString("99051341-9e82-4790-8b77-44896241b886"), 0);
		oldUsers.put(UUID.fromString("9b11c3f3-9e55-4a84-984e-f688ad11ebb0"), 1);
		oldUsers.put(UUID.fromString("9b2610c6-9640-4ec0-bd74-1e1a733aa12c"), 1);
		oldUsers.put(UUID.fromString("9bb03570-257b-48fc-9337-df5640cc225b"), 1);
		oldUsers.put(UUID.fromString("9c0ef83a-0859-4736-ad25-75663751f90c"), 1);
		oldUsers.put(UUID.fromString("9ce9df51-a967-456b-ac26-7f52fa1d6099"), 5);
		oldUsers.put(UUID.fromString("9d5827f4-d503-4b27-b03b-8b6395c761ce"), 1);
		oldUsers.put(UUID.fromString("9ebcd2e0-6c97-47bd-936f-ddc2c916a85f"), 1);
		oldUsers.put(UUID.fromString("a0258f54-1d21-4349-9b31-0fd4efcc0deb"), 4);
		oldUsers.put(UUID.fromString("a18f6b19-a64f-46f6-83af-beef76d12d03"), 1);
		oldUsers.put(UUID.fromString("a218015c-a628-40b9-9829-c7542585370e"), 1);
		oldUsers.put(UUID.fromString("a267657f-883b-441b-9727-0a6c91caffff"), 2);
		oldUsers.put(UUID.fromString("a39908df-a0ef-461e-a912-8cac25e3454c"), 4);
		oldUsers.put(UUID.fromString("a4254802-1daa-43a0-9216-48ba768faa38"), 3);
		oldUsers.put(UUID.fromString("a4a4c8ea-43f0-4cd2-80ff-72da0b78445e"), 9);
		oldUsers.put(UUID.fromString("a68edcd7-3f83-4e43-a025-bc85a6156727"), 1);
		oldUsers.put(UUID.fromString("a6ee63e0-ee53-436c-ba49-fdcd6e27c87b"), 6);
		oldUsers.put(UUID.fromString("a732c5ff-8348-48a4-9e5a-746e39a61be9"), 10);
		oldUsers.put(UUID.fromString("a7e7e921-a584-489c-a653-99eb599eee12"), 1);
		oldUsers.put(UUID.fromString("a81507ad-bf39-4de8-bd58-e27dd86def0b"), 4);
		oldUsers.put(UUID.fromString("aa577d57-dc6b-4358-b43d-16f1b520f29d"), 7);
		oldUsers.put(UUID.fromString("abee4cb5-15b7-4216-a9bf-36d7b00b4122"), 0);
		oldUsers.put(UUID.fromString("ac3274f2-f4ba-42ce-b67e-49e84424dae0"), 1);
		oldUsers.put(UUID.fromString("ac36575a-06ea-4f91-9b79-450c4d76b94f"), 9);
		oldUsers.put(UUID.fromString("ac741cbf-65b7-45b5-a000-15a7886949b3"), 1);
		oldUsers.put(UUID.fromString("acc996c1-3053-4cc9-99ce-fbe6c5e96858"), 6);
		oldUsers.put(UUID.fromString("ad3638b3-c3d2-477d-a65c-b188eb8a43c5"), 1);
		oldUsers.put(UUID.fromString("ae12392c-2c0f-4783-986f-f80d1ccc61e0"), 0);
		oldUsers.put(UUID.fromString("aef4e44f-8a8f-4987-989a-57b4515745f5"), 2);
		oldUsers.put(UUID.fromString("af6247b5-c016-463c-a356-47ac5b64f75f"), 1);
		oldUsers.put(UUID.fromString("af70b92a-4ce3-454d-9862-57b67d074460"), 1);
		oldUsers.put(UUID.fromString("afd05bc7-9d7f-4076-beda-d8e84c014752"), 2);
		oldUsers.put(UUID.fromString("b12be14e-bf0f-4640-bea0-d54af818351c"), 13);
		oldUsers.put(UUID.fromString("b15b2196-2f70-4cc0-b2c5-c54e715f0afb"), 1);
		oldUsers.put(UUID.fromString("b2c965a4-ed4d-42bf-b5a9-9400e157212a"), 1);
		oldUsers.put(UUID.fromString("b4665e99-287c-45e2-b031-6d7489b09c0b"), 20);
		oldUsers.put(UUID.fromString("b54090db-b704-401f-9d16-dca8ad8a0045"), 0);
		oldUsers.put(UUID.fromString("b5e169d9-ad4e-4479-aba8-fdc644f52c60"), 2);
		oldUsers.put(UUID.fromString("b5fbeee4-4092-4de0-9678-ae1dd44b878e"), 1);
		oldUsers.put(UUID.fromString("b76167e9-b9ce-49b0-951a-7c318e694289"), 1);
		oldUsers.put(UUID.fromString("b824e65e-8509-46ec-a31d-4ab1821ac232"), 0);
		oldUsers.put(UUID.fromString("b8dfba6c-b430-4fd6-a23b-e02d02e5e3e0"), 4);
		oldUsers.put(UUID.fromString("b9f6efd4-e713-4f0f-8abf-028491018136"), 1);
		oldUsers.put(UUID.fromString("b9f86c06-46a6-4433-80be-70d4cdfe09ec"), 1);
		oldUsers.put(UUID.fromString("bb4d6c75-f004-4e3d-96d7-876c9e0c6a54"), 1);
		oldUsers.put(UUID.fromString("bc1b12cc-23e0-4fbb-addf-59032d5fb641"), 6);
		oldUsers.put(UUID.fromString("bc30caa7-6ef3-409a-a28a-f37a1abd67f0"), 1);
		oldUsers.put(UUID.fromString("bd1e740a-e353-4031-884e-e6b17fa62300"), 11);
		oldUsers.put(UUID.fromString("bd4343b6-52d3-4810-8525-6a7b7196ba76"), 0);
		oldUsers.put(UUID.fromString("bda604ed-ea82-4438-8a3a-a3a250a71965"), 6);
		oldUsers.put(UUID.fromString("c41bae79-4322-4563-9973-c9838d95c13f"), 1);
		oldUsers.put(UUID.fromString("c578ce83-3808-4753-9d3a-84f9eef4be2f"), 0);
		oldUsers.put(UUID.fromString("c5cba95f-bd21-4dd5-b0ae-7dc31f28923c"), 0);
		oldUsers.put(UUID.fromString("c5fc51e9-81eb-4f77-929d-bf341c56983d"), 6);
		oldUsers.put(UUID.fromString("c6013db0-9373-4494-8256-317d6a9edd5f"), 1);
		oldUsers.put(UUID.fromString("c6c5e02d-5cdf-4008-959c-9fc0a1e9a851"), 1);
		oldUsers.put(UUID.fromString("c7138e8d-2944-4080-a777-de6d804d3649"), 1);
		oldUsers.put(UUID.fromString("c86313c2-ad1e-4754-84db-3abbd223a6a7"), 4);
		oldUsers.put(UUID.fromString("c9e9e053-598e-452a-87cd-4e152437b241"), 3);
		oldUsers.put(UUID.fromString("cad5a75d-07f8-4aaf-8e99-fd10b159a385"), 1);
		oldUsers.put(UUID.fromString("cb1721af-e4d5-4fd5-b872-d2d7835adf6c"), 1);
		oldUsers.put(UUID.fromString("cd0d73dc-62a5-461e-a54a-4b67e5d4c476"), 2);
		oldUsers.put(UUID.fromString("cfea76c9-bc9d-4b60-979e-fa66229b8914"), 4);
		oldUsers.put(UUID.fromString("d09744af-691b-49c9-9775-a20b6a1aa1b7"), 0);
		oldUsers.put(UUID.fromString("d0df8ef7-3670-4b99-8da6-6bfd99ee8699"), 1);
		oldUsers.put(UUID.fromString("d1251e03-ca82-4514-888e-d62db8e3de9c"), 0);
		oldUsers.put(UUID.fromString("d1be7d4d-1d47-4d83-89aa-416cf454d59f"), 9);
		oldUsers.put(UUID.fromString("d2b13303-ef31-46f4-9f31-364030db8a86"), 1);
		oldUsers.put(UUID.fromString("d3b5c392-bdd7-484f-9c9e-74049e1b97b0"), 10);
		oldUsers.put(UUID.fromString("d4369693-84c6-4e61-935d-90934c8b973e"), 1);
		oldUsers.put(UUID.fromString("d5146713-4053-43f2-9aa1-a4e6a2f2530a"), 1);
		oldUsers.put(UUID.fromString("d78de063-97d6-48f6-a699-a75ca35c1bcf"), 1);
		oldUsers.put(UUID.fromString("db1a9471-48e2-4c58-b963-5713dcd17c58"), 5);
		oldUsers.put(UUID.fromString("dda4adbf-f12c-4c0e-bc89-16d604c1ab5f"), 1);
		oldUsers.put(UUID.fromString("ddf622d1-016f-4add-8e3c-1ae3270f00f9"), 4);
		oldUsers.put(UUID.fromString("de84304e-a30a-4ea7-b887-b6a24d7b02de"), 4);
		oldUsers.put(UUID.fromString("dfb413ad-0ce4-4877-876e-ac9e596e8a62"), 5);
		oldUsers.put(UUID.fromString("e01f379f-2482-4fe3-ae6b-26dd4d0c9f36"), 8);
		oldUsers.put(UUID.fromString("e1823d4e-4bd2-47b2-98a4-255729e9ce13"), 1);
		oldUsers.put(UUID.fromString("e3cecc0d-561b-4262-b793-9250d1dbca65"), 4);
		oldUsers.put(UUID.fromString("e49e69a6-810d-49be-9226-4dce147151f3"), 1);
		oldUsers.put(UUID.fromString("e6287cd2-503e-4360-8277-db45d1dca965"), 5);
		oldUsers.put(UUID.fromString("e678d703-fd29-4c5a-ac2e-af4bfff23b55"), 0);
		oldUsers.put(UUID.fromString("e7495e89-483f-4ebb-b2a2-97f0c69db572"), 0);
		oldUsers.put(UUID.fromString("e7fb7a5a-18fe-4a25-8621-8c5f86a9d43e"), 1);
		oldUsers.put(UUID.fromString("e8b1f41e-34c3-4793-b3fe-9a92ec80d238"), 1);
		oldUsers.put(UUID.fromString("e8f7a7dd-036c-47db-970e-29c6c66a56ec"), 2);
		oldUsers.put(UUID.fromString("e9094344-b663-4ebf-b318-8dc06298c177"), 2);
		oldUsers.put(UUID.fromString("ea63e264-43b1-47c1-b01b-902728faac72"), 1);
		oldUsers.put(UUID.fromString("eacd7d9a-6b58-492d-abe6-7e0f4cc00bba"), 2);
		oldUsers.put(UUID.fromString("eb75fe95-9f21-4e35-9c28-86f549f256cf"), 11);
		oldUsers.put(UUID.fromString("ebadc407-442d-478c-b0fa-8886301a5ea5"), 1);
		oldUsers.put(UUID.fromString("edd5e9aa-1ac4-4aa7-8039-20645f7c9b1e"), 1);
		oldUsers.put(UUID.fromString("ede0ebce-1bc3-42b0-8673-fe0f6dc6e2be"), 0);
		oldUsers.put(UUID.fromString("ee957666-04f6-42c6-a809-4edd93130e3f"), 7);
		oldUsers.put(UUID.fromString("ee9bdb8a-c8c8-4710-822d-13049c168e20"), 1);
		oldUsers.put(UUID.fromString("ef88ba1c-dc2a-4d31-82b9-81c9284ba310"), 1);
		oldUsers.put(UUID.fromString("f0307564-e6d2-4368-be46-272d15dc92f5"), 1);
		oldUsers.put(UUID.fromString("f0615d10-1cb5-4a9e-91e0-6ab75a42b26f"), 1);
		oldUsers.put(UUID.fromString("f0ca7c60-aa0b-4f0e-83c7-7fb09b4d84ff"), 1);
		oldUsers.put(UUID.fromString("f2a8e71c-df84-44ba-8126-a7a06d8f099d"), 6);
		oldUsers.put(UUID.fromString("f443fa5d-c5c4-4530-8b70-8832b3a34b60"), 0);
		oldUsers.put(UUID.fromString("f49dfd77-f27d-4d43-b93c-590d49636845"), 1);
		oldUsers.put(UUID.fromString("f604ebe7-60aa-43cf-92cf-540314a9d22e"), 0);
		oldUsers.put(UUID.fromString("f6159e9a-00fb-4723-ae70-a8e37c63f445"), 1);
		oldUsers.put(UUID.fromString("f79d9412-daf0-4b6b-b06c-6005fca9e6f8"), 1);
		oldUsers.put(UUID.fromString("f9f8f1fc-ab5a-40de-8ec5-929e58bbe198"), 1);
		oldUsers.put(UUID.fromString("faf58056-6e98-4e08-b25e-b19638af1873"), 5);
		oldUsers.put(UUID.fromString("fbca74db-f446-4b07-93c0-3304cd5634f4"), 1);
		oldUsers.put(UUID.fromString("fe557cd8-050b-4a89-93f9-5c0b70ba584d"), 1);
		oldUsers.put(UUID.fromString("ff56f6d7-603c-4dd0-ac03-ad3387b8a7a4"), 0);
		oldUsers.put(UUID.fromString("ff6d2a26-0efc-4583-9224-b0c71fa42d96"), 22);
	}
	
	public static boolean isCBTTester(Player p) {
		return cbtTesters.contains(p.getUniqueId());
	}
	
	public static boolean isOBTTester(Player p) {
		return obtTesters.contains(p.getUniqueId());
	}
	
	public static boolean isOldUser(Player p) {
		return oldUsers.containsKey(p.getUniqueId());
	}
	
	public static int getOldLevel(Player p) {
		return oldUsers.get(p.getUniqueId());
	}
}