package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeCommentResponseDto {

    private Long id;
    private String content;
    private int heartCount;
    private String nickname;
    private Long perfumeId;
    private String profileImg;
    private boolean isLiked;
    private boolean isWrited;

    public PerfumeCommentResponseDto(PerfumeComment perfumeComment,boolean isLiked,Member member,String profileImg){
        this.id=perfumeComment.getId();
        this.content=perfumeComment.getContent();
        this.heartCount=perfumeComment.getHeartCount();
        this.nickname=perfumeComment.getMember().getNickname();
        this.perfumeId=perfumeComment.getPerfume().getId();
        this.isLiked = isLiked;
        if(perfumeComment.getMember().getMemberPhoto()==null){
            this.profileImg=profileImg;
        }else {
            this.profileImg = perfumeComment.getMember().getMemberPhoto().getPhotoUrl();
        }
        if (member==null){
            this.isWrited=false;
        }else{
            if(perfumeComment.getMember().getId()== member.getId()){
                this.isWrited=true;
            }else this.isWrited=false;
        }
    }

}
