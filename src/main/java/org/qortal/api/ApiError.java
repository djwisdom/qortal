package org.qortal.api;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public enum ApiError {
	// COMMON
	// UNKNOWN(0, 500),
	JSON(1, 400),
	INSUFFICIENT_BALANCE(2, 402),
	// NOT_YET_RELEASED(3, 422),
	UNAUTHORIZED(4, 403),
	REPOSITORY_ISSUE(5, 500),
	NON_PRODUCTION(6, 403),
	BLOCKCHAIN_NEEDS_SYNC(7, 503),
	NO_TIME_SYNC(8, 503),

	// VALIDATION
	INVALID_SIGNATURE(101, 400),
	INVALID_ADDRESS(102, 400),
	// INVALID_SEED(103, 400),
	// INVALID_AMOUNT(104, 400),
	// INVALID_FEE(105, 400),
	// INVALID_SENDER(106, 400),
	// INVALID_RECIPIENT(107, 400),
	// INVALID_NAME_LENGTH(108, 400),
	// INVALID_VALUE_LENGTH(109, 400),
	// INVALID_NAME_OWNER(110, 400),
	// INVALID_BUYER(111, 400),
	INVALID_PUBLIC_KEY(112, 400),
	// INVALID_OPTIONS_LENGTH(113, 400),
	// INVALID_OPTION_LENGTH(114, 400),
	INVALID_DATA(115, 400),
	// INVALID_DATA_LENGTH(116, 400),
	// INVALID_UPDATE_VALUE(117, 400),
	// KEY_ALREADY_EXISTS(118, 422),
	// KEY_NOT_EXISTS(119, 404),
	// LAST_KEY_IS_DEFAULT_KEY_ERROR(120, 422),
	// FEE_LESS_REQUIRED(121, 422),
	// WALLET_NOT_IN_SYNC(122, 422),
	INVALID_NETWORK_ADDRESS(123, 404),
	ADDRESS_UNKNOWN(124, 404),
	INVALID_CRITERIA(125, 400),
	INVALID_REFERENCE(126, 400),
	TRANSFORMATION_ERROR(127, 400),
	INVALID_PRIVATE_KEY(128, 400),
	INVALID_HEIGHT(129, 400),
	CANNOT_MINT(130, 400),

	// WALLET
	// WALLET_NO_EXISTS(201, 404),
	// WALLET_ADDRESS_NO_EXISTS(202, 404),
	// WALLET_LOCKED(203, 422),
	// WALLET_ALREADY_EXISTS(204, 422),
	// WALLET_API_CALL_FORBIDDEN_BY_USER(205, 403),

	// BLOCKS
	BLOCK_UNKNOWN(301, 404),

	// TRANSACTIONS
	TRANSACTION_UNKNOWN(311, 404),
	PUBLIC_KEY_NOT_FOUND(304, 404),
	TRANSACTION_INVALID(312, 400),

	// NAMING
	NAME_UNKNOWN(401, 404),
	// NAME_ALREADY_EXISTS(402, 422),
	// NAME_ALREADY_FOR_SALE(403, 422),
	// NAME_NOT_LOWER_CASE(404, 422),
	// NAME_SALE_NO_EXISTS(410, 404),
	// BUYER_ALREADY_OWNER(411, 422),

	// POLLS
	// POLL_NO_EXISTS(501, 404),
	// POLL_ALREADY_EXISTS(502, 422),
	// DUPLICATE_OPTION(503, 422),
	// POLL_OPTION_NO_EXISTS(504, 404),
	// ALREADY_VOTED_FOR_THAT_OPTION(505, 422),

	// ASSET
	INVALID_ASSET_ID(601, 400),
	INVALID_ORDER_ID(602, 400),
	ORDER_UNKNOWN(603, 404),

	// NAME PAYMENTS
	// NAME_NOT_REGISTERED(701, 422),
	// NAME_FOR_SALE(702, 422),
	// NAME_WITH_SPACE(703, 422),

	// ATs
	// INVALID_DESC_LENGTH(801, 400),
	// EMPTY_CODE(802, 400),
	// DATA_SIZE(803, 400),
	// NULL_PAGES(804, 400),
	// INVALID_TYPE_LENGTH(805, 400),
	// INVALID_TAGS_LENGTH(806, 400),
	// INVALID_CREATION_BYTES(809, 400),

	// BLOG/Namestorage
	// BODY_EMPTY(901, 400),
	// BLOG_DISABLED(902, 403),
	// NAME_NOT_OWNER(903, 422),
	// TX_AMOUNT(904, 400),
	// BLOG_ENTRY_NO_EXISTS(905, 404),
	// BLOG_EMPTY(906, 404),
	// POSTID_EMPTY(907, 400),
	// POST_NOT_EXISTING(908, 404),
	// COMMENTING_DISABLED(909, 403),
	// COMMENT_NOT_EXISTING(910, 404),
	// INVALID_COMMENT_OWNER(911, 422),

	// Messages
	// MESSAGE_FORMAT_NOT_HEX(1001, 400),
	// MESSAGE_BLANK(1002, 400),
	// NO_PUBLIC_KEY(1003, 422),
	// MESSAGESIZE_EXCEEDED(1004, 400),

	// Groups
	GROUP_UNKNOWN(1101, 404),

	// Foreign blockchain
	FOREIGN_BLOCKCHAIN_NETWORK_ISSUE(1201, 500),
	FOREIGN_BLOCKCHAIN_BALANCE_ISSUE(1202, 402),
	FOREIGN_BLOCKCHAIN_TOO_SOON(1203, 408),

	// Trade portal
	ORDER_SIZE_TOO_SMALL(1300, 402),

	// Data
	FILE_NOT_FOUND(1401, 404),
	NO_REPLY(1402, 404);

	private static final Map<Integer, ApiError> map = stream(ApiError.values()).collect(toMap(apiError -> apiError.code, apiError -> apiError));

	private final int code; // API error code
	private final int status; // HTTP status code

	private ApiError(int code) {
		this(code, 400); // defaults to "400 - BAD REQUEST"
	}

	private ApiError(int code, int status) {
		this.code = code;
		this.status = status;
	}

	public static ApiError fromCode(int code) {
		return map.get(code);
	}

	public int getCode() {
		return this.code;
	}

	public int getStatus() {
		return this.status;
	}

}
