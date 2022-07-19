package jpql;


import jdk.swing.interop.SwingInterOpUtils;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {

    public JpaMain() {
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
/*
            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            em.persist(member);


            // 결과 조회 API
            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            List<Member> resultList1 = query.getResultList(); // 컬렉션 반환
            for (Member member1 : resultList1) {
                System.out.println("member1 = " + member1);
            }

            TypedQuery<Member> singleQuery = em.createQuery("select m from Member m where m.id = 1", Member.class);
            Member singleResult1 = singleQuery.getSingleResult(); // 값이 정확히 1개
            System.out.println("singleResult1 = " + singleResult1);
            // Spring Data JPA에서 제공하는 함수에서는 결과가 없으면 NULL이나 Optional 반환


            // 파라미터 바인딩
            Member singleResult2 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult2 = " + singleResult2.getUsername());


            em.flush();
            em.clear();
            // 영속성 컨텍스트 초기화


            // 프로젝션
            List<Member> resultList2 = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            Member findMember = resultList2.get(0);
            findMember.setAge(20); // 바뀜 = 영속성 컨텍스트에서 관리가 됨

            em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();
            em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList(); // 예측 가능하도록, 최대한 SQL 문법과 비슷하게 써야 함

            em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();


            // 프로젝션 - 여러 값 조회
            // 1. Query 타입으로 조회
            List resultList3 = query3.getResultList();
            Object o = resultList3.get(0);
            Object[] result = (Object[]) o; // 타입 캐스팅
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

            // 2. Object[] 타입으로 조회
            List<Object[]> resultList4 = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();
            Object o2 = resultList4.get(0);
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

            // 3. new
            List<MemberDTO> resultList5 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            MemberDTO memberDTO = resultList5.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());
*/


/*
            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            // 페이징
            String query4 = "select m from Member m order by m.age desc";
            List<Member> resultList6 = em.createQuery(query4, Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
            System.out.println("resultList6.size() = " + resultList6.size());
            for (Member member1 : resultList6) {
                System.out.println("member1 = " + member1);
            }
*/


/*
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.setTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            em.flush();
            em.clear();


            // 조인
            // 1. 내부 조인
            String query5 = "select m from Member m inner join m.team t"; // inner 생략 가능
            List<Member> resultList7 = em.createQuery(query5, Member.class)
                                        // 지연 로딩을 설정해놓지 않으면 쿼리가 1개 더 나감
                    .getResultList();
            // 응용
            String query6 = "select m from Member m inner join m.team t where t.name = :teamName";

            // 2. 외부 조인
            String query7 = "select m from Member m left outer join m.team t"; // outer 생략 가능
            em.createQuery(query5, Member.class)
                    .getResultList();

            // 3. 세타 조인
            String query8 = "select count(m) from Member m, Team t where m.username = t.name";
            Long singleResult3 = em.createQuery(query8, Long.class) // => cross join
                    .getSingleResult();
            System.out.println("singleResult3 = " + singleResult3);

            // ON - 1. 조인 대상 필터링
            String query9 = "select m from Member m left join m.team t on t.name = 'teamA'";
            em.createQuery(query9, Member.class)
                    .getResultList();
            // ON - 2. 연관관계 없는 엔티티 외부 조인
            String query10 = "select m from Member m left join Team t on m.username = t.name";
            em.createQuery(query10, Member.class)
                    .getResultList();


            // 서브 쿼리
            // SELECT절 가능
            String query11 = "select (select avg(m1.age) from Member m1) as avgAge from Member m left join Team t on m.username = t.name";
            // FROM절 서브 쿼리는 불가능
            String query12 = "select mm.age, mm.username from (select m.age, m.username from Member m) as mm";


            // JPQL 타입 표현
            String query13 = "select m.username, 'HELLO', true From Member m " +
                    "where m.type = :userType";
            List<Object[]> resultList8 = em.createQuery(query13)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();
            // 왜 나는 자동완성 시킬때 List로만..?

            for (Object[] objects : resultList8) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[1] = " + objects[1]);
                System.out.println("objects[2] = " + objects[2]);
            }


            // 타입
            // ITEM - BOOK, BOOK과 관려된 것만 알고싶을 경우
            // em.createQuery("select i from Item i where type(i) = Book", Item.class);


            // 조건식
            // 단순 CASE식
            String query14 =
                    "select " +
                        "case when m.age <= 10 then '학생요금' " +
                            "when m.age >= 60 then '경로요금' " +
				            "else '일반요금' " +
                        "end " +
                    "from Member m";

            // NULLIF
            String query15 = "select nullif(m.username,'teamA') from Member m";
            List<String> resultList9 = em.createQuery(query15, String.class).getResultList();
            for (String s : resultList9) {
                System.out.println("s = " + s);
            }


            // JPQL 기본 함수
            String query16 = "select 'a' || 'b' From Member m";
            List<String> resultList10 = em.createQuery(query16, String.class).getResultList();
            for (String s : resultList10) {
                System.out.println("s = " + s);
            }


            // 사용자 정의 함수 호출


            // 경로 표현식
            // 단일 값 연관 경로
            String query17 = "select m.team From Member m"; // member에 연관된 team을 가져옴
            List<Team> resultList11 = em.createQuery(query17, Team.class).getResultList();
            for (Team t : resultList11) {
                System.out.println("t = " + t);
            }

            // 컬렉션 값 연관 경로
            String query18 = "select t.members From Team t";
            List<Collection> resultList12 = em.createQuery(query18, Collection.class)
                    .getResultList(); // 실제로 이렇게 쓰지 않음
            System.out.println("resultList12 = " + resultList12);


            String query19 = "select t.members.size From Team t";
            Integer singleResult = em.createQuery(query19, Integer.class)
                    .getSingleResult();
            System.out.println("singleResult = " + singleResult);

            // 명시적 조인
            String query20 = "select m.username From Team t join t.members m";
            List<String> resultList13 = em.createQuery(query20, String.class).getResultList();
            System.out.println("resultList13 = " + resultList13);
*/


            // 페치 조인인
           Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();


            // 컬렉션 페치 조인
            String query21 = "select m from Member m join fetch m.team";
            List<Member> resultList14 = em.createQuery(query21, Member.class)
                    .getResultList();
            for (Member member : resultList14) {
                System.out.println("username = " + member.getUsername() +
                        ", teamName = " + member.getTeam().getName());
            }


            // 중복 제거
            String query22 = "select t from Team t join fetch t.members where t.name='팀A'";
            List<Team> resultList15 = em.createQuery(query22, Team.class)
                    .getResultList();
            for (Team team : resultList15) {
                System.out.println("username = " + team.getName() +
                        " | team = " + team);
                for (Member member : team.getMembers()) {
                    System.out.println("-> username = " + member.getUsername() +
                            ", member = " + member);
                }
            }

            // 중복 제거
            String query23 = "select distinct t from Team t join fetch t.members where t.name='팀A'";
            List<Team> resultList16 = em.createQuery(query23, Team.class)
                    .getResultList();
            System.out.println("result = " + resultList16.size());
            for (Team team : resultList16) {
                System.out.println("username = " + team.getName() +
                        " | team = " + team);
                for (Member member : team.getMembers()) {
                    System.out.println("-> username = " + member.getUsername() +
                            ", member = " + member);
                }
            }


            // 페치 조인과 일반 조인의 차이
            // 일반 조인
            String query24 = "select t from Team t join t.members m where t.name='팀A'";
            List<Team> resultList17 = em.createQuery(query24, Team.class)
                    .getResultList();

            System.out.println("result = " + resultList17.size());

            for (Team team : resultList17) {
                System.out.println("username = " + team.getName() +
                        " | team = " + team);
                for (Member member : team.getMembers()) {
                    System.out.println("-> username = " + member.getUsername() +
                            ", member = " + member);
                }
            }


            // 페이징 API
            String query25 = "select t from Team t join fetch t.members m";
//            List<Team> resultList18 = em.createQuery(query25, Team.class)
//                    .setFirstResult(0)
//                    .setMaxResults(1)
//                    .getResultList(); // 쿼리를 보면, 페이징을 하지 않음
//
//            System.out.println("result = " + resultList18.size());
//
//            for (Team team : resultList18) {
//                System.out.println("username = " + team.getName() +
//                        " | team = " + team);
//                for (Member member : team.getMembers()) {
//                    System.out.println("-> username = " + member.getUsername() +
//                            ", member = " + member);
//                }
//            }

            // 다대일로 뒤집음
            String query26 = "select m from Member m join fetch m.team t";

            em.flush();
            em.clear();

            // 다른 방법
            String query27 = "select t from Team t";
            List<Team> resultList19 = em.createQuery(query27, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("result = " + resultList19.size());

            for (Team team : resultList19) {
                System.out.println("username = " + team.getName() +
                        " | team = " + team);
                for (Member member : team.getMembers()) {
                    System.out.println("-> username = " + member.getUsername() +
                            ", member = " + member);
                }
            }


            // 엔티티 직접 사용 - 기본 키 값
            // 엔티티를 파라미터로 전달
            String query28 = "select m from Member m where m = :member";
            Member findMember2 = em.createQuery(query28, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            System.out.println("findMember2 = " + findMember2);

            // 식별자를 직접 전달
            String query29 = "select m from Member m where m.id = :memberId";
            Member findMember3 = em.createQuery(query29, Member.class)
                    .setParameter("memberId", member1.getId())
                    .getSingleResult();
            System.out.println("findMember3 = " + findMember3);


            // 엔티티 직접 사용 - 외래 키 값
            String query30 = "select m from Member m where m.team = :team";
            List<Member> resultList20 = em.createQuery(query30, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();
            for (Member member : resultList20) {
                System.out.println("member = " + member);
            }


            // Named 쿼리
            List<Member> resultList21 = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();
            for (Member member : resultList21) {
                System.out.println("member = " + member);
            }

//            em.flush();
//            em.clear();

            // 벌크연산
            // FLUSH 자동 호출
            // flush는 commit, query가 나갈 때 자동호출하거나 강제호출해 사용할 수 있다
            // flush가 나가고 update문이 나가기 때문에 영속성 컨텍스트에 있는 것에 대해 고민하지 않아도 된다.
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate(); // DB에 강제로 업데이트
            // DB에만 업데이트됨
            System.out.println("resultCount = " + resultCount);
            // 영속성 컨텍스트엔 20살이 반영되어있지 않음
            System.out.println("member1.getAge() = " + member1.getAge());
            System.out.println("member1.getAge() = " + member2.getAge());
            System.out.println("member1.getAge() = " + member3.getAge());

            // 영속성 초기화 후 다시 값 불러옴
            em.clear();
            Member member = em.find(Member.class, member1.getId());
            System.out.println("member.getAge() = " + member.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }



        emf.close();
    }
}