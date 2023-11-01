package org.jeecg.modules.im.base.xmpp;

import lombok.Data;

@Data
public class MessageBean {
	private String content;
	private Integer userId;
	private String userName;
	private long tsSend = System.currentTimeMillis();
	private Integer toUserId;
	private String toUserName;
	//1:执行 0:取消
	private int flag;
	private int isEncrypt=0;
	// 文字，图片，链接。。。。。参见MsgType
	private int type;
	//UUID
	private String stanzaId;

	//消息type  0：普通单聊消息    1：群组消息    2：广播消息
	private int msgType;

	private Integer mucId;// 群组id


	public enum MsgType{
		chat(0),groupChat(1),broadcast(2);
		int code;
		MsgType(int code){
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}
	}


}