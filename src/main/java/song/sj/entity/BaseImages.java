package song.sj.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import song.sj.TimeStamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class BaseImages extends TimeStamp {

    private String imageName;
    private String serverImageName;
    private String imageType;

    public BaseImages(String imageName, String serverImageName, String imageType) {
        this.imageName = imageName;
        this.serverImageName = serverImageName;
        this.imageType = imageType;
    }
}
