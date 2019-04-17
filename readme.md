## � �������

����� ���������� ������� �������� ��������� ������������� ��������� �������� ������� ������ ����� � ������� ����������� ��. � ������� ���� ����������� 6 ��������� ������� ������� (������ `m0()..m5()` � ������ `Insert.java`), ������ �� ������� ������������ ������ � ������� 1000 �����, ��������� �� ���������� �����, ������������� ����, � ��������� ���������� ���� ���������� ����� (`VARCHAR(32)`). ������ ������ ������������ ����� ���������� ������� ���������� �������, � ���������� �������� (� *��*) ������������ � ���� ��������� ��������� ������. ��������� ����������� ��� ���� ����� ����: PostgreSQL 9.3, ������������ ���� H2 � ������ ������ � �������� ������, � H2 � ������ in-memory.

������ ������ ������� (`m0()..m3()`) �������� �� ������������� ������� JDBC. � ������ `m0()` ������� ������ ������ �������������� ��������� ����������� (� ������ ������������ ������) � �������������� ������� `Statement`. � ������ `m1()` ����� ������������ ������ `Statement`, ������ ��� �������� ����������� ������ ����� ����������. � ������ `m2()` ������� �������������� � �������������� `PreparedStatement` (������ ����� ����������). � ������ `m3()` ����� ������������ �������������� ���������, �� ������ ��� ���� �������������� � �������� (batch) ������.

����� `m4()` ��� ������� ����� ���������� �������� ORM (Hibernate). �, �������, � ������ `m5()` ������� �������������� � �������������� ������ `JdbcTemplate` �� ���������� Spring. 

### �������� ����������

�� ������� 1 ������������ ���������� ��������� (� *��*) ��� PostgreSQL.

![img1](../resources/images/img1.png "������� 1000 ����� ��� PostgreSQL")

�� �������� ������� ��������� �������� �����������:

    <Stm>..<Stm> - ������� � ������ AutoCommit � �������������� Statement;
    <Stm> - ������� ������ ����� ���������� � �������������� Statement;
    <PrStm> - ������� ������ ����� ���������� � �������������� PreparedStatement;
    <Batch> - ������� ������ ����� ���������� � �������������� PreparedStatement � batch-������;
    <Hibernate> - ������� � �������������� Hibernate;
    <JdbcTempl> - ������� � �������������� JdbcTemplate.

���������� �������� ��������� ����� ���������� ������ `createStatement()` � `prepareStatement()`, � ����� �������`commit()`. ��������������� ������ ��� ���� H2 �������� �� �������� 2, 3.

![img2](../resources/images/img2.png "������� 1000 ����� ��� H2 ��� ������ � ������")

![img3](../resources/images/img3.png "������� 1000 ����� ��� H2 � ������ in-memory")

�� ����������� �������� �����, ��� � ����� ������ ������������������ �������� ������������ �������� ������� �������� `JdbcTemplate` ��� batch-����� � JDBC. ������������� ��������������� ������� (Hibernate) ����������� ����� ������� � 4-6 ��� �� ��������� � ���������� ��������� ��� ������ ����. ����� ����, ����� ������ ��� ������������ ���� (��� ������ � ������) � 2-5 ��� ������, ��� ��� ������� ����. ��� ������ � ������� ��� ������� ������������� �� 10-15 ���.

��� ���������� �������� (����� `Plotter.java`) �������������� ��������� ������� gnuplot, ������� (������� � ������ 5.0.1) ������������� ����������� ����� ��� ����� ������ � ������ �� �������� ����������.