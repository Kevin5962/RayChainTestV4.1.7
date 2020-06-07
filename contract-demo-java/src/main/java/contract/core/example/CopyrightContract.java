package contract.core.example;

import cn.mrray.raybaas.contract.base.server.service.ContractStub;
import cn.mrray.raybaas.contract.base.vo.ContractVo;
import cn.mrray.raybaas.contract.base.vo.Response;
import com.alibaba.fastjson.JSON;

import static contract.core.example.CopyrightContract.GlobalCode.*;

/**
 * Description: 版权合约样例
 *
 * @author rannuo1010@gmail.com
 * @date 2019/12/04
 */
public class CopyrightContract {

	private final static String COPYRIGHT = "copyright:";
	private final static String WARRANT = "warrant:";

	/**
	 * 初始化账户
	 *
	 * @param contractStub 合约与状态库的交互
	 * @param contractVo   合约调用信息
	 * @return 执行结果
	 */
	public Response initAccount(ContractStub contractStub, ContractVo contractVo) {
		// 1：获取合约的传参（参数[data]为JSON字符串）
		Account account = parseParams(contractVo, Account.class);

		// 2：检查参与运算的变量值（具体的校验逻辑根据业务来决定）
		if (isBlank(account.getId())) {
			return error(String.format(ACCOUNT_MISTAKEN.msg(), account.getId()));
		}

		// 3：准备写入到状态库的key和value（经过共识后最终写入到状态库）
		contractStub.putState(account.getId(), contractVo.getData());

		// 4：返回执行成功的结果
		return success();
	}

	/**
	 * 查询账户
	 *
	 * @param contractStub 合约与状态库的交互
	 * @param contractVo   合约调用信息
	 * @return 执行结果
	 */
	public Response queryAccount(ContractStub contractStub, ContractVo contractVo) {
		// 1：获取合约的传参（参数为JSON字符串）
		Account account = parseParams(contractVo, Account.class);

		// 2：检查参与运算的变量值（根据业务来决定）
		if (isBlank(account.getId())) {
			return error(ACCOUNT_ID_IS_EMPTY.msg());
		}

		// 3：返回执行结果
		return success(contractStub.getState(account.getId()));
	}

	/**
	 * 版权登记确权
	 *
	 * @param contractStub 合约与状态库的交互
	 * @param contractVo   合约调用信息
	 * @return 执行结果
	 */
	public Response right(ContractStub contractStub, ContractVo contractVo) {
		// 1：获取合约的传参（参数[data]为JSON字符串）
		Copyright copyright = parseParams(contractVo, Copyright.class);

		// 2：检查参与运算的变量值（具体的校验逻辑根据业务来决定）
		if (!copyright.check()) {
			return error(COPYRIGHT_WARRANT_MISTAKEN.msg());
		}

		// 3：验证签名
		String sign = copyright.getSign();
		if (!verifySign(sign)) {
			return error(String.format(COPYRIGHT_SIGN_VERIFY_FAIL.msg(), sign));
		}

		// 4：准备写入到状态库的key和value（经过共识后最终写入到状态库）
		// 4.1 记录版权
		contractStub.putState(copyright.getId(), contractVo.getData());
		// 4.2 记录版权确权信息
		contractStub.putState(COPYRIGHT + copyright.getAccountId() + ":" + copyright.getId(), contractVo.getData());

		// 5：返回执行成功的结果
		return success();
	}

	/**
	 * 版权授权
	 *
	 * @param contractStub 合约与状态库的交互
	 * @param contractVo   合约调用信息
	 * @return 执行结果
	 */
	public Response warrant(ContractStub contractStub, ContractVo contractVo) {
		// 1：获取合约的传参（参数[data]为JSON字符串）
		Warrant warrant = parseParams(contractVo, Warrant.class);

		// 2：检查参与运算的变量值（具体的校验逻辑根据业务来决定）
		if (!warrant.check()) {
			return error(COPYRIGHT_MISTAKEN.msg());
		}

		// 3：查询授权人和被授权人账户信息
		String warrantPartyId = contractStub.getState(warrant.getWarrantPartyId());
		if (isBlank(warrantPartyId)) {
			return error(String.format(ACCOUNT_NOT_EXIST.msg(), warrant.getWarrantPartyId()));
		}
		String warrantedPartyId = contractStub.getState(warrant.getWarrantedPartyId());
		if (isBlank(warrantedPartyId)) {
			return error(String.format(ACCOUNT_NOT_EXIST.msg(), warrant.getWarrantedPartyId()));
		}
		String copyrightId = warrant.getCopyrightId();

		// 4：检查授权人与授权版权是否对应
		String copyrightJson = contractStub.getState(copyrightId);
		if (isBlank(copyrightJson)) {
			return error(String.format(COPYRIGHT_NOT_EXIST.msg(), copyrightId));
		}
		String copyrightWarrantJson = contractStub.getState(COPYRIGHT + warrantPartyId + ":" + copyrightId);
		if (isBlank(copyrightWarrantJson)) {
			return error(String.format(COPYRIGHT_NOT_MATCH_ACCOUNT.msg(), copyrightId, warrantPartyId));
		}

		// 4：准备写入到状态库的key和value（经过共识后最终写入到状态库）
		contractStub.putState(WARRANT + warrantedPartyId + ":" + copyrightId, "true");

		// 5：返回执行成功的结果
		return success();
	}

	/**
	 * 查询版权
	 *
	 * @param contractStub 合约与状态库的交互
	 * @param contractVo   合约调用信息
	 * @return 执行结果
	 */
	public Response queryCopyright(ContractStub contractStub, ContractVo contractVo) {
		// 1：获取合约的传参（参数为JSON字符串）
		Copyright copyright = parseParams(contractVo, Copyright.class);

		// 2：检查参与运算的变量值（根据业务来决定）
		String copyrightId = copyright.getId();
		if (isBlank(copyrightId)) {
			return error(COPYRIGHT_ID_IS_EMPTY.msg());
		}
		String accountId = copyright.getAccountId();
		if (isBlank(accountId)) {
			return error(ACCOUNT_ID_IS_EMPTY.msg());
		}

		// 3：检查账户是否具有版权授权
		// 3.1 查询版权的确权信息
		String copyrightJson = contractStub.getState(COPYRIGHT + accountId + ":" + copyrightId);
		if (isBlank(copyrightJson)) {
			// 3.2 不是自己的版权，则查询授权信息
			copyrightJson = contractStub.getState(WARRANT + accountId + ":" + copyrightId);
		}
		// 3.3 既不是自己的版权，也未被授权，直接返回失败
		if (isBlank(copyrightJson)) {
			return error(String.format(COPYRIGHT_UNAUTHORIZED_ACCOUNT.msg(), copyrightId, accountId));
		}
		// 4：返回执行结果
		return success(contractStub.getState(copyrightId));
	}

	/**
	 * 验证签名
	 *
	 * @param sign
	 * @return
	 */
	private boolean verifySign(String sign) {
		if (isBlank(sign)) {
			return false;
		}
		// 使用创作方的公钥解密
		return true;
	}

	/**
	 * 是否为空
	 *
	 * @return
	 */
	private static boolean isBlank(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * 解析合约传参
	 *
	 * @param contractVo 合约调用信息对象
	 * @return T类型的对象
	 */
	private static <T> T parseParams(final ContractVo contractVo, final Class<T> clazz) {
		// 使用RayBaaS底层支持的fastjson
		return JSON.parseObject(contractVo.getData(), clazz);
	}

	private static Response success() {
		return success(SUCCESS.msg(), null);
	}

	private static Response success(String payload) {
		return success(SUCCESS.msg(), payload);
	}

	private static Response success(String message, String payload) {
		return response(Response.SUCCESS_STATUS, message, payload);
	}

	private static Response error(String message) {
		if (null == message) {
			message = FAILURE.msg();
		}
		return response(Response.ERROR_STATUS, message, null);
	}

	private static Response response(int code, String message, String payload) {
		return new Response(code, message, payload);
	}

	/**
	 * 全局枚举常量
	 */
	public enum GlobalCode {
		// 操作相关
		SUCCESS(200, "操作成功！"),
		FAILURE(500, "操作失败！"),
		// 账户相关
		ACCOUNT_MISTAKEN(1000, "账户[%]有误，请检查账户是否为空"),
		ACCOUNT_ID_IS_EMPTY(1001, "账户ID为空"),
		ACCOUNT_NOT_EXIST(1001, "账户[%s]不存在"),
		// 版权相关
		COPYRIGHT_MISTAKEN(1100, "版权信息有误"),
		COPYRIGHT_SIGN_VERIFY_FAIL(1101, "版权信息签名[%s]验证失败"),
		COPYRIGHT_ID_IS_EMPTY(1102, "版权ID为空"),
		COPYRIGHT_WARRANT_MISTAKEN(1003, "版权授权信息有误"),
		COPYRIGHT_NOT_MATCH_ACCOUNT(1004, "版权[%s]与账户[%s]不一匹配"),
		COPYRIGHT_NOT_EXIST(1005, "版权[%s]不存在"),
		COPYRIGHT_UNAUTHORIZED_ACCOUNT(1006, "版权[%s]未授权给此账户[%s]"),
		;

		private int value;
		private String msg;

		GlobalCode(int value, String msg) {
			this.value = value;
			this.msg = msg;
		}

		public int value() {
			return value;
		}

		public String msg() {
			return msg;
		}
	}

	/**
	 * 账户信息
	 */
	public static class Account {
		/**
		 * 用户ID
		 */
		private String id;
		/**
		 * 用户名
		 */
		private String name;
		/**
		 * 机构
		 */
		private String agency;
		/**
		 * 私钥
		 */
		private String priKey;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAgency() {
			return agency;
		}

		public void setAgency(String agency) {
			this.agency = agency;
		}

		public String getPriKey() {
			return priKey;
		}

		public void setPriKey(String priKey) {
			this.priKey = priKey;
		}
	}

	/**
	 * 版权信息
	 */
	public static class Copyright {
		/**
		 * 唯一ID
		 */
		private String id;
		/**
		 * 归属账户ID
		 */
		private String accountId;
		/**
		 * 版权归属
		 */
		private String attribution;
		/**
		 * 作品名称
		 */
		private String worksName;
		/**
		 * 作品哈希
		 */
		private String worksHash;
		/**
		 * 签名
		 */
		private String sign;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getAccountId() {
			return accountId;
		}

		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}

		public String getAttribution() {
			return attribution;
		}

		public void setAttribution(String attribution) {
			this.attribution = attribution;
		}

		public String getWorksName() {
			return worksName;
		}

		public void setWorksName(String worksName) {
			this.worksName = worksName;
		}

		public String getWorksHash() {
			return worksHash;
		}

		public void setWorksHash(String worksHash) {
			this.worksHash = worksHash;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		boolean check() {
			return !isBlank(id) && !isBlank(accountId) && !isBlank(this.attribution)
					&& !isBlank(this.worksName) && !isBlank(this.worksHash);
		}
	}

	/**
	 * 授权
	 */
	public static class Warrant {
		/**
		 * 授权方
		 */
		private String warrantPartyId;
		/**
		 * 被授权方
		 */
		private String warrantedPartyId;
		/**
		 * 版权ID
		 */
		private String copyrightId;

		public String getWarrantPartyId() {
			return warrantPartyId;
		}

		public void setWarrantPartyId(String warrantPartyId) {
			this.warrantPartyId = warrantPartyId;
		}

		public String getWarrantedPartyId() {
			return warrantedPartyId;
		}

		public void setWarrantedPartyId(String warrantedPartyId) {
			this.warrantedPartyId = warrantedPartyId;
		}

		public String getCopyrightId() {
			return copyrightId;
		}

		public void setCopyrightId(String copyrightId) {
			this.copyrightId = copyrightId;
		}

		boolean check() {
			return true;
		}
	}
	
	private static class TestVO {
		private String name;
		private int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
	
	public Response test(ContractStub contractStub, ContractVo contractVo){
		return success(JSON.toJSONString(new TestVO()));
	}
}
