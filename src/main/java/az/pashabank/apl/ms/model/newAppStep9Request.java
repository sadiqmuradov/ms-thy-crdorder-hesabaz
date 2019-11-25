package az.pashabank.apl.ms.model;

import az.pashabank.apl.ms.entity.UploadWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class newAppStep9Request {
    private List<UploadWrapper> fileUploads;
}
