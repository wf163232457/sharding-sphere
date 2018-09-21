/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.opentracing.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import io.opentracing.Span;
import io.opentracing.tag.Tags;
import io.shardingsphere.core.event.connection.CloseConnectionEvent;
import io.shardingsphere.core.event.connection.CloseConnectionStartEvent;
import io.shardingsphere.opentracing.ShardingTags;

/**
 * Connection close event listener.
 *
 * @author zhangyonglun
 */
public final class CloseConnectionEventListener extends OpenTracingListener<CloseConnectionEvent> {
    
    private static final String OPERATION_NAME = "/" + ShardingTags.COMPONENT_NAME + "/closeConnection/";
    
    public CloseConnectionEventListener() {
        super(OPERATION_NAME);
    }
    
    /**
     * Listen close connection event.
     *
     * @param event Close connection event
     */
    @Subscribe
    @AllowConcurrentEvents
    public void listen(final CloseConnectionEvent event) {
        tracing(event);
    }
    
    @Override
    protected void initSpan(final CloseConnectionEvent event, final Span span) {
        CloseConnectionStartEvent startEvent = (CloseConnectionStartEvent) event;
        span.setTag(Tags.DB_INSTANCE.getKey(), startEvent.getDataSource())
                .setTag(Tags.PEER_HOSTNAME.getKey(), startEvent.getDataSourceMetaData().getHostName())
                .setTag(Tags.PEER_PORT.getKey(), startEvent.getDataSourceMetaData().getPort());
    }
}