package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeCommentResponseDto {
    private Long id;
    private String comment;
    private int heart;
    private Long member_id;
    private Long perfume_id;

    public PerfumeCommentResponseDto(PerfumeComment perfumeComment){
        this.id=perfumeComment.getId();
        this.comment=perfumeComment.getComment();
        this.heart=perfumeComment.getHeart();
        this.member_id=perfumeComment.getMember().getId();
        this.perfume_id=perfumeComment.getPerfume().getId();
    }

}