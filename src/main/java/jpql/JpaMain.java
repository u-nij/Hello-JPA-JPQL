package jpql;


import jdk.swing.interop.SwingInterOpUtils;

import javax.persistence.*;
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