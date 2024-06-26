package hmoa.hmoaserver.magazine.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Magazine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "magazine_id")
    private long id;
    private String title;
    private String preview;
    private String previewImgUrl;
    private int viewCount;
    private int likeCount;
    @ElementCollection
    private List<String> tags;
    @OneToMany(mappedBy = "magazine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MagazineContent> contents = new ArrayList<>();

    @OneToMany(mappedBy = "magazine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MagazineLikedMember> likedMembers = new ArrayList<>();

    @Builder
    public Magazine(String title, List<MagazineContent> contents, List<String> tags) {
        this.title = title;
        this.contents = contents;
        this.tags = tags;
        this.viewCount = 0;
        this.likeCount = 0;
    }

    public void setPreviews(String preview, String previewImgUrl) {
        this.preview = preview;
        this.previewImgUrl = previewImgUrl;
    }

    public void setContents(List<MagazineContent> contents) {
        this.contents = contents;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}
