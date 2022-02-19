drop keyspace if exists akka;

CREATE KEYSPACE IF NOT EXISTS akka
    WITH REPLICATION = { 'class' : 'SimpleStrategy','replication_factor': 1 };

CREATE TABLE IF NOT EXISTS akka.messages (
                                             persistence_id text,
                                             partition_nr bigint,
                                             sequence_nr bigint,
                                             timestamp timeuuid,
                                             timebucket text,
                                             writer_uuid text,
                                             ser_id int,
                                             ser_manifest text,
                                             event_manifest text,
                                             event blob,
                                             meta_ser_id int,
                                             meta_ser_manifest text,
                                             meta blob,
                                             tags set<text>,
                                             PRIMARY KEY ((persistence_id, partition_nr), sequence_nr, timestamp))
    WITH gc_grace_seconds =864000
     AND compaction = {
        'class' : 'SizeTieredCompactionStrategy',
        'enabled' : true,
        'tombstone_compaction_interval' : 86400,
        'tombstone_threshold' : 0.2,
        'unchecked_tombstone_compaction' : false,
        'bucket_high' : 1.5,
        'bucket_low' : 0.5,
        'max_threshold' : 32,
        'min_threshold' : 4,
        'min_sstable_size' : 50
        };

CREATE TABLE IF NOT EXISTS akka.tag_views (
                                              tag_name text,
                                              persistence_id text,
                                              sequence_nr bigint,
                                              timebucket bigint,
                                              timestamp timeuuid,
                                              tag_pid_sequence_nr bigint,
                                              writer_uuid text,
                                              ser_id int,
                                              ser_manifest text,
                                              event_manifest text,
                                              event blob,
                                              meta_ser_id int,
                                              meta_ser_manifest text,
                                              meta blob,
                                              PRIMARY KEY ((tag_name, timebucket), timestamp, persistence_id, tag_pid_sequence_nr))
    WITH gc_grace_seconds =864000
     AND compaction = {
        'class' : 'SizeTieredCompactionStrategy',
        'enabled' : true,
        'tombstone_compaction_interval' : 86400,
        'tombstone_threshold' : 0.2,
        'unchecked_tombstone_compaction' : false,
        'bucket_high' : 1.5,
        'bucket_low' : 0.5,
        'max_threshold' : 32,
        'min_threshold' : 4,
        'min_sstable_size' : 50
        };

CREATE TABLE IF NOT EXISTS akka.tag_write_progress(
                                                      persistence_id text,
                                                      tag text,
                                                      sequence_nr bigint,
                                                      tag_pid_sequence_nr bigint,
                                                      offset timeuuid,
                                                      PRIMARY KEY (persistence_id, tag));

CREATE TABLE IF NOT EXISTS akka.tag_scanning(
                                                persistence_id text,
                                                sequence_nr bigint,
                                                PRIMARY KEY (persistence_id));

CREATE TABLE IF NOT EXISTS akka.metadata(
                                            persistence_id text PRIMARY KEY,
                                            deleted_to bigint,
                                            properties map<text,text>);

CREATE TABLE IF NOT EXISTS akka.all_persistence_ids(
    persistence_id text PRIMARY KEY);

drop keyspace if exists akka_snapshot;

CREATE KEYSPACE IF NOT EXISTS akka_snapshot
    WITH REPLICATION = { 'class' : 'SimpleStrategy','replication_factor': 1 };

CREATE TABLE IF NOT EXISTS akka_snapshot.snapshots (
                                                       persistence_id text,
                                                       sequence_nr bigint,
                                                       timestamp bigint,
                                                       ser_id int,
                                                       ser_manifest text,
                                                       snapshot_data blob,
                                                       snapshot blob,
                                                       meta_ser_id int,
                                                       meta_ser_manifest text,
                                                       meta blob,
                                                       PRIMARY KEY (persistence_id, sequence_nr))
    WITH CLUSTERING ORDER BY (sequence_nr DESC) AND gc_grace_seconds =864000
     AND compaction = {
        'class' : 'SizeTieredCompactionStrategy',
        'enabled' : true,
        'tombstone_compaction_interval' : 86400,
        'tombstone_threshold' : 0.2,
        'unchecked_tombstone_compaction' : false,
        'bucket_high' : 1.5,
        'bucket_low' : 0.5,
        'max_threshold' : 32,
        'min_threshold' : 4,
        'min_sstable_size' : 50
        };

drop keyspace if exists d_optimas;

create KEYSPACE d_optimas WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1 };
use d_optimas;

create table message_state (
                               id uuid,
                               time bigint,
                               timestamp bigint,
                               entity_id text,
                               message_type text,
                               status text,
                               primary key ( id, entity_id,  status )
);

create table global_state (
                              id uuid,
                              time bigint,
                              mean double,
                              variance double,
                              solutions bigint,
                              regions int,
                              region_ids list<int>,
                              primary key ( id, time )
) with clustering order by (time asc);

create table region_state (
                              id uuid,
                              name text ,
                              time bigint,
                              best_solution uuid,
                              mean double,
                              variance double,
                              solutions list<uuid>,
                              num_solutions bigint,
                              primary key ((id, name), time, best_solution )
) with clustering order by (time asc);

create table solution_state (
                                id uuid,
                                agent text,
                                region text,
                                time bigint,
                                values list<double>,
                                function_value double,
                                primary key ( (id), agent, region, time )
) with clustering order by (agent asc, region asc, time asc);

create table agent_state (
                                id uuid,
                                persistent_id text,
                                algorithm_name text,
                                time bigint,
                                timestamp_start bigint,
                                timestamp_end bigint,
                                produced_solution uuid,
                                function_evaluations bigint,
                                primary key ((id), persistent_id, time)

) with clustering order by (persistent_id asc, time asc);

create table memory_state (
                                id uuid,
                                time bigint,
                                agent text,
                                regions list<int>,
                                probabilities list<double>,
                                best_solution_id uuid,
                                memory_status text,
                                chosen_region int,
                                primary key ((id), agent, time )
) with clustering order by (agent asc, time asc);