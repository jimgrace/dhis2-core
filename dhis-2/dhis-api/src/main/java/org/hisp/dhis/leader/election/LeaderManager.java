/*
 * Copyright (c) 2004-2022, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.leader.election;

/**
 * Manages cluster leader node elections, renewals, revocations and to check whether the current
 * instance is the leader in the cluster.
 *
 * @author Ameen Mohamed
 */
public interface LeaderManager {
  /** Extend the expiration time of leadership if this node is the current leader. */
  void renewLeader(int ttlSeconds);

  /** Attempt to become the leader. */
  void electLeader(int ttlSeconds);

  /**
   * Check if the current instance is the leader.
   *
   * @return true if this instance is the leader, false otherwise.
   */
  boolean isLeader();

  /**
   * Get the nodeID that was generated for the current instance.
   *
   * @return the nodeID
   */
  String getCurrentNodeUuid();

  /**
   * Get the nodeID for the current leader instance in the cluster.
   *
   * @return the nodeID of the leader instance.
   */
  String getLeaderNodeUuid();

  /**
   * Get the nodeID for the current leader instance in the cluster.
   *
   * @return the nodeID of the leader instance.
   */
  String getLeaderNodeId();
}
