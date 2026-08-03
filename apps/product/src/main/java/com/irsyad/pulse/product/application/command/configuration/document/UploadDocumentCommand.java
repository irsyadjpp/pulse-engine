package com.irsyad.pulse.product.application.command.configuration.document;

import java.util.UUID;

/**
 * Command to upload Product Document metadata (FSD_03 Section 10).
 */
public record UploadDocumentCommand(
        UUID productId,
        String documentName,
        String documentType,
        String storageReference) {
}
