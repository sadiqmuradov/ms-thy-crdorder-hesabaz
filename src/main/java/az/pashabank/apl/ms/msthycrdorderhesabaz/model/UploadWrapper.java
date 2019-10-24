package az.pashabank.apl.ms.msthycrdorderhesabaz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadWrapper {

    private byte[] bytes;
    private long size;
    private String name;
    private String location;
    private String contentType;

}
