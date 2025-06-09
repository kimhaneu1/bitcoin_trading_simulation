package org.example.backend.global.exception.requestError.asset;

import org.example.backend.global.exception.ErrorCode;
import org.example.backend.global.exception.requestError.BusinessException;

public class AssetNotFoundException extends BusinessException {
    public AssetNotFoundException() {
        super(ErrorCode.ASSET_NOT_FOUND);
    }
}
