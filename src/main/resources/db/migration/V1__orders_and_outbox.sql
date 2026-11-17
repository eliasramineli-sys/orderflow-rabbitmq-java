CREATE TABLE customer_orders (
  id BIGSERIAL PRIMARY KEY,
  customer_name VARCHAR(120) NOT NULL,
  item_name VARCHAR(150) NOT NULL,
  quantity INTEGER NOT NULL CHECK (quantity > 0),
  status VARCHAR(20) NOT NULL CHECK (status IN ('CREATED', 'COMPLETED')),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  completed_at TIMESTAMP WITH TIME ZONE,
  version BIGINT NOT NULL DEFAULT 0
);
CREATE TABLE outbox_events (
  id UUID PRIMARY KEY,
  order_id BIGINT NOT NULL REFERENCES customer_orders(id),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  published_at TIMESTAMP WITH TIME ZONE
);
CREATE INDEX idx_outbox_unpublished ON outbox_events(created_at) WHERE published_at IS NULL;
