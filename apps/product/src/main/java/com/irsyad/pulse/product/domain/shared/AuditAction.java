package com.irsyad.pulse.product.domain.shared;

/**
 * Audit action types recorded in the Audit Trail.
 *
 * <p>Referenced from FSD_05 (Section 16 Audit Action).
 */
public enum AuditAction {

    CREATE,
    UPDATE,
    PUBLISH,
    ARCHIVE,
    ACTIVATE,
    DEACTIVATE,
    VERSION_CREATED,
    DOCUMENT_UPLOAD,
    DOCUMENT_DELETE
}