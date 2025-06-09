package org.example.backend.global.exception.requestError.asset;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.BusinessException;

public class AssetListEmptyException extends BusinessException {
    public AssetListEmptyException(String message) {
        super(ErrorCode.ASSET_LIST_EMPTY);
    }
}
