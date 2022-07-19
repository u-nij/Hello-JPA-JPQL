package jpql;

import javax.persistence.*;

@Entity
@NamedQuery( // Named 쿼리
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private int age;

    // 양방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void changeTeam(Team team) { // 양방향 연관관계로 설정
        this.team = team;
        team.getMembers().add(this);
    }

    @Enumerated(EnumType.STRING)
    public MemberType type;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
//                ", team=" + team + // 양방향에서 무한루프 주의! 삭제하는 것이 좋다.
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public MemberType getType() {
        return type;
    }

    public void setType(MemberType type) {
        this.type = type;
    }
}
