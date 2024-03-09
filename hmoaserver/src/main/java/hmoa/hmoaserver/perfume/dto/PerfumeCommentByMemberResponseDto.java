package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeCommentByMemberResponseDto {
    private Long id;
    private String content;
    private int heartCount;
    private String nickname;
    private Long parentId;
    private String profileImg;
    private boolean isLiked = false;
    private boolean isWrited = false;
    private String createAt;

    public PerfumeCommentByMemberResponseDto(PerfumeComment perfumeComment, boolean isLiked, Member member) {
        this.id = perfumeComment.getId();
        this.content = perfumeComment.getContent();
        this.heartCount = perfumeComment.getHeartCount();
        this.nickname = perfumeComment.getMember().getNickname();
        this.parentId = perfumeComment.getPerfume().getId();
        this.isLiked = isLiked;
        this.createAt = DateUtils.calcurateDaysAgo(perfumeComment.getCreatedAt());
        this.profileImg = perfumeComment.getMember().getMemberPhoto().getPhotoUrl();
        this.isWrited = perfumeComment.getMember().getId().equals(member.getId());
    }
}
