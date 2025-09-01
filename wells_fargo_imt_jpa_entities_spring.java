// Package note: adjust the base package to match your scaffold (e.g., com.wf.imt or com.example.app)
// Below I use: package com.wf.imt.entities;

// ─────────────────────────────────────────────────────────────────────────────
// ENUMS
// ─────────────────────────────────────────────────────────────────────────────
package com.wf.imt.entities;

public enum SecurityCategory {
    EQUITY,
    BOND,
    ETF,
    MUTUAL_FUND,
    CASH,
    OTHER
}

package com.wf.imt.entities;

public enum TransactionType {
    BUY,
    SELL,
    ADJUSTMENT
}

package com.wf.imt.entities;

public enum AuditAction {
    CREATE,
    UPDATE,
    DELETE
}

// ─────────────────────────────────────────────────────────────────────────────
// ENTITY: FinancialAdvisor
// ─────────────────────────────────────────────────────────────────────────────
package com.wf.imt.entities;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "financial_advisors",
       indexes = {
           @Index(name = "idx_advisor_username", columnList = "username", unique = true),
           @Index(name = "idx_advisor_email", columnList = "email", unique = true)
       })
public class FinancialAdvisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advisor_id")
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(nullable = false, length = 64)
    private String timezone = "UTC";

    @Column(name = "business_hours_start", nullable = false)
    private LocalTime businessHoursStart = LocalTime.of(9, 0);

    @Column(name = "business_hours_end", nullable = false)
    private LocalTime businessHoursEnd = LocalTime.of(17, 0);

    // Bitmask for Mon..Sun, default Mon–Fri: 0b0111110 = 62
    @Column(name = "business_days", nullable = false)
    private Short businessDays = 62;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "advisor")
    private List<Client> clients = new ArrayList<>();

    protected FinancialAdvisor() { /* JPA */ }

    public FinancialAdvisor(String username,
                            String email,
                            String fullName,
                            String timezone,
                            LocalTime businessHoursStart,
                            LocalTime businessHoursEnd,
                            Short businessDays,
                            boolean isActive) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.timezone = timezone;
        this.businessHoursStart = businessHoursStart;
        this.businessHoursEnd = businessHoursEnd;
        this.businessDays = businessDays;
        this.isActive = isActive;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    // Getters (no setId per spec) and Setters
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public LocalTime getBusinessHoursStart() { return businessHoursStart; }
    public void setBusinessHoursStart(LocalTime businessHoursStart) { this.businessHoursStart = businessHoursStart; }

    public LocalTime getBusinessHoursEnd() { return businessHoursEnd; }
    public void setBusinessHoursEnd(LocalTime businessHoursEnd) { this.businessHoursEnd = businessHoursEnd; }

    public Short getBusinessDays() { return businessDays; }
    public void setBusinessDays(Short businessDays) { this.businessDays = businessDays; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public List<Client> getClients() { return clients; }
    public void setClients(List<Client> clients) { this.clients = clients; }
}

// ─────────────────────────────────────────────────────────────────────────────
// ENTITY: Client
// ─────────────────────────────────────────────────────────────────────────────
package com.wf.imt.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "clients",
       indexes = { @Index(name = "idx_client_advisor", columnList = "advisor_id") })
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "advisor_id", nullable = false)
    private FinancialAdvisor advisor;

    @Column(name = "first_name", length = 128)
    private String firstName;

    @Column(name = "last_name", length = 128)
    private String lastName;

    @Column(length = 255)
    private String email;

    @Column(length = 32)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Portfolio portfolio;

    protected Client() { /* JPA */ }

    public Client(FinancialAdvisor advisor,
                  String firstName,
                  String lastName,
                  String email,
                  String phone,
                  LocalDate dateOfBirth,
                  boolean isActive) {
        this.advisor = advisor;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.isActive = isActive;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }

    public FinancialAdvisor getAdvisor() { return advisor; }
    public void setAdvisor(FinancialAdvisor advisor) { this.advisor = advisor; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }
}

// ─────────────────────────────────────────────────────────────────────────────
// ENTITY: Portfolio
// ─────────────────────────────────────────────────────────────────────────────
package com.wf.imt.entities;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios",
       uniqueConstraints = { @UniqueConstraint(name = "uk_portfolio_client", columnNames = {"client_id"}) },
       indexes = { @Index(name = "idx_portfolio_client", columnList = "client_id") })
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private Client client;

    @Column(name = "display_name", length = 128)
    private String displayName = "Main";

    @Column(length = 3)
    private String currency = "USD";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioHolding> holdings = new ArrayList<>();

    protected Portfolio() { /* JPA */ }

    public Portfolio(Client client, String displayName, String currency) {
        this.client = client;
        this.displayName = displayName;
        this.currency = currency;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public List<PortfolioHolding> getHoldings() { return holdings; }
    public void setHoldings(List<PortfolioHolding> holdings) { this.holdings = holdings; }
}

// ─────────────────────────────────────────────────────────────────────────────
// ENTITY: SecurityMaster
// ─────────────────────────────────────────────────────────────────────────────
package com.wf.imt.entities;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "security_master",
       indexes = {
           @Index(name = "idx_security_ticker", columnList = "ticker"),
           @Index(name = "idx_security_isin", columnList = "isin")
       })
public class SecurityMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "security_id")
    private Long id;

    @Column(length = 32)
    private String ticker;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SecurityCategory category;

    @Column(length = 50)
    private String isin;

    @Column(length = 3)
    private String currency = "USD";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "security")
    private List<PortfolioHolding> holdings = new ArrayList<>();

    protected SecurityMaster() { /* JPA */ }

    public SecurityMaster(String ticker,
                          String name,
                          SecurityCategory category,
                          String isin,
                          String currency) {
        this.ticker = ticker;
        this.name = name;
        this.category = category;
        this.isin = isin;
        this.currency = currency;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public SecurityCategory getCategory() { return category; }
    public void setCategory(SecurityCategory category) { this.category = category; }

    public String getIsin() { return isin; }
    public void setIsin(String isin) { this.isin = isin; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public List<PortfolioHolding> getHoldings() { return holdings; }
    public void setHoldings(List<PortfolioHolding> holdings) { this.holdings = holdings; }
}

// ─────────────────────────────────────────────────────────────────────────────
// ENTITY: PortfolioHolding
// ─────────────────────────────────────────────────────────────────────────────
package com.wf.imt.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolio_holdings",
       indexes = {
           @Index(name = "idx_holding_portfolio", columnList = "portfolio_id"),
           @Index(name = "idx_holding_security", columnList = "security_id")
       })
public class PortfolioHolding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holding_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "security_id", nullable = false)
    private SecurityMaster security;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "purchase_price", nullable = false, precision = 19, scale = 6)
    private BigDecimal purchasePrice;

    @Column(nullable = false, precision = 20, scale = 6)
    private BigDecimal quantity;

    @Column(length = 3)
    private String currency = "USD";

    @Column(name = "average_cost", precision = 19, scale = 6)
    private BigDecimal averageCost;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "holding", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    protected PortfolioHolding() { /* JPA */ }

    public PortfolioHolding(Portfolio portfolio,
                            SecurityMaster security,
                            LocalDate purchaseDate,
                            BigDecimal purchasePrice,
                            BigDecimal quantity,
                            String currency,
                            BigDecimal averageCost,
                            boolean isActive) {
        this.portfolio = portfolio;
        this.security = security;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.currency = currency;
        this.averageCost = averageCost;
        this.isActive = isActive;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }

    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }

    public SecurityMaster getSecurity() { return security; }
    public void setSecurity(SecurityMaster security) { this.security = security; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getAverageCost() { return averageCost; }
    public void setAverageCost(BigDecimal averageCost) { this.averageCost = averageCost; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
}

// ─────────────────────────────────────────────────────────────────────────────
// ENTITY: Transaction
// ─────────────────────────────────────────────────────────────────────────────
package com.wf.imt.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions",
       indexes = { @Index(name = "idx_tx_holding_date", columnList = "holding_id, transaction_date") })
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "holding_id", nullable = false)
    private PortfolioHolding holding;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 16)
    private TransactionType transactionType;

    @Column(name = "transaction_date", nullable = false)
    private OffsetDateTime transactionDate;

    @Column(nullable = false, precision = 20, scale = 6)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal price; // per unit

    @Column(precision = 19, scale = 6)
    private BigDecimal fees = BigDecimal.ZERO;

    @Lob
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private FinancialAdvisor createdBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected Transaction() { /* JPA */ }

    public Transaction(PortfolioHolding holding,
                       TransactionType transactionType,
                       OffsetDateTime transactionDate,
                       BigDecimal quantity,
                       BigDecimal price,
                       BigDecimal fees,
                       String notes,
                       FinancialAdvisor createdBy) {
        this.holding = holding;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.quantity = quantity;
        this.price = price;
        this.fees = fees;
        this.notes = notes;
        this.createdBy = createdBy;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }

    public PortfolioHolding getHolding() { return holding; }
    public void setHolding(PortfolioHolding holding) { this.holding = holding; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public OffsetDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(OffsetDateTime transactionDate) { this.transactionDate = transactionDate; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getFees() { return fees; }
    public void setFees(BigDecimal fees) { this.fees = fees; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public FinancialAdvisor getCreatedBy() { return createdBy; }
    public void setCreatedBy(FinancialAdvisor createdBy) { this.createdBy = createdBy; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
}

// ─────────────────────────────────────────────────────────────────────────────
// ENTITY: AuditLog
// ─────────────────────────────────────────────────────────────────────────────
package com.wf.imt.entities;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_logs",
       indexes = {
           @Index(name = "idx_audit_entity", columnList = "entity, entity_id"),
           @Index(name = "idx_audit_performed_at", columnList = "performed_at")
       })
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long id;

    @Column(nullable = false, length = 64)
    private String entity;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private AuditAction action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private FinancialAdvisor performedBy;

    @Column(name = "performed_at", nullable = false)
    private OffsetDateTime performedAt;

    @Lob
    private String changes; // JSON payload or diff

    protected AuditLog() { /* JPA */ }

    public AuditLog(String entity,
                    Long entityId,
                    AuditAction action,
                    FinancialAdvisor performedBy,
                    OffsetDateTime performedAt,
                    String changes) {
        this.entity = entity;
        this.entityId = entityId;
        this.action = action;
        this.performedBy = performedBy;
        this.performedAt = performedAt;
        this.changes = changes;
    }

    public Long getId() { return id; }

    public String getEntity() { return entity; }
    public void setEntity(String entity) { this.entity = entity; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public AuditAction getAction() { return action; }
    public void setAction(AuditAction action) { this.action = action; }

    public FinancialAdvisor getPerformedBy() { return performedBy; }
    public void setPerformedBy(FinancialAdvisor performedBy) { this.performedBy = performedBy; }

    public OffsetDateTime getPerformedAt() { return performedAt; }
    public void setPerformedAt(OffsetDateTime performedAt) { this.performedAt = performedAt; }

    public String getChanges() { return changes; }
    public void setChanges(String changes) { this.changes = changes; }
}

// ─────────────────────────────────────────────────────────────────────────────
// NOTES:
// - JPA requires a no-arg constructor (protected) — included in every entity.
// - IDs are auto-generated (IDENTITY). If your scaffold uses UUIDs, switch to
//   @Id @GeneratedValue and a UUID column (Hibernate-specific generators) and
//   change Long entityId in AuditLog accordingly.
// - Each field has @Column or a relationship annotation as required.
// - createdAt/updatedAt are managed by @PrePersist/@PreUpdate.
// - Decimal fields use BigDecimal for precision.
// - One-to-one Portfolio↔Client is enforced via unique constraint on client_id.
